package com.walker.concurrency.artof.chapter4base.webserver;

import com.walker.concurrency.artof.chapter4base.threadpool.DefaultThreadPool;
import com.walker.concurrency.artof.chapter4base.threadpool.ThreadPool;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by walker on 2017/1/31.
 */
public class SimpleHttpServer {
    //线程池
    static ThreadPool<HttpRequestHandler> threadPool = new DefaultThreadPool<HttpRequestHandler>();
    //根路径
    static String basePath;
    //服务监听端口
    static int port = 8080;

    public static void setPort(int port) {
        if(port <= 0)return;
        SimpleHttpServer.port = port;
    }

    public static void setBasePath(String basePath) {
        if (basePath != null && new File(basePath).exists() && new File(basePath).isDirectory()) {
            SimpleHttpServer.basePath = basePath;
        }
    }

    //启动服务器
    public static void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port); //Creates a server socket, bound to the specified port.
        Socket socket = null;
        while((socket = serverSocket.accept())!=null){
            threadPool.execute(new HttpRequestHandler(socket));
        }
        serverSocket.close();
    }

    /**
     * 注意理解：HttpRequestHandler是job，而不是worker！！！
     */
    private static class HttpRequestHandler implements Runnable{
        private Socket socket;//需要处理的socket连接

        public HttpRequestHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            String line = null;
            BufferedReader reader = null;
            PrintWriter out = null;
            InputStream fileIn = null;
            BufferedReader br = null;

            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String header = reader.readLine();
                //计算绝对路径
                String filePath = basePath + header.split(" ")[1];
                out = new PrintWriter(socket.getOutputStream());
                if (filePath.endsWith("jpg") || filePath.endsWith("ico")) {
                    fileIn = new FileInputStream(filePath);   //字节流
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    int i = 0;
                    while ((i = fileIn.read()) != -1) {
                        baos.write(i);
                    }
                    byte[] array = baos.toByteArray();
                    out.println("HTTP/1.1 200 OK");
                    out.println("Server: walker's Cat");
                    out.println("Content-Type: image/jpeg");
                    out.println("Content-Length: " + array.length);
                    out.println("");
                    socket.getOutputStream().write(array, 0, array.length);
                } else {
                    br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath))); //字符流
                    out.println("HTTP/1.1 200 OK");
                    out.println("Server: walker's Cat");
                    out.println("Content-Type: image/jpeg");
                    out.println("");
                    while ((line = br.readLine()) != null) {
                        out.println(line);
                    }
                }
                out.flush();
            } catch (Exception e) {
                out.println("HTTP/1.1 500");
                out.println("");
                out.flush();
            }finally {
                close(br,fileIn,out,reader,socket);
            }
        }

        private static void close(Closeable... closeables) {
            for (Closeable closeable : closeables) {
                if (closeable != null) {
                    try {
                        closeable.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
