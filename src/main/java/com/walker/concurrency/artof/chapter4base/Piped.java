package com.walker.concurrency.artof.chapter4base;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

/**
 * Created by walker on 2017/1/29.
 */
public class Piped {
    public static void main(String[] args) throws IOException {
        PipedWriter writer = new PipedWriter();;
        PipedReader reader = new PipedReader();
        try {
            //连接管道
            writer.connect(reader);
            //启动reader
            new Thread(new Print(reader)).start();
            //向管道里write
            int receive = 0;
            while((receive = System.in.read())!= -1){
                writer.write(receive);
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            reader.close();
            writer.close();
        }
    }

    private static final class Print implements Runnable {
        private PipedReader reader;

        public Print(PipedReader reader) {
            this.reader = reader;
        }

        public void run() {
            try {
                int receive = 0;
                while((receive = reader.read())!= -1){
                    System.out.print((char)receive);
                }
                System.out.println();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
