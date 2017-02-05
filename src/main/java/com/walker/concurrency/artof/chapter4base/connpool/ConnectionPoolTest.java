package com.walker.concurrency.artof.chapter4base.connpool;

import java.sql.Connection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by walker on 2017/1/30.
 */
public class ConnectionPoolTest {
    private static ConnectionPool pool = new ConnectionPool(10);
    private static final int threadSize = 30;
    private static final int times = 20;
    private static CountDownLatch start = new CountDownLatch(1);
    private static CountDownLatch end = new CountDownLatch(threadSize);

    public static void main(String[] args) throws InterruptedException {

        AtomicInteger got = new AtomicInteger(0);
        AtomicInteger notGot = new AtomicInteger(0);
        for(int i=0;i<threadSize;i++) {
            Thread t = new Thread(new ConnectionRunner(20,got,notGot), String.valueOf(i));
            t.start();
        }
        TimeUnit.SECONDS.sleep(1); //先歇会

        start.countDown(); //预备，开始！

        end.await();//等待结束
        System.out.println("threadSize:"+threadSize+" totalTries:"+threadSize*times+" succ:"+got.get()+" fail:"+notGot.get()+
                " fail percent:"+notGot.get()*10000/(threadSize*times)/100.0+"%");
    }

    static class ConnectionRunner implements Runnable {
        private int times; //获取连接次数
        private AtomicInteger got;
        private AtomicInteger notGot;

        public ConnectionRunner(int times, AtomicInteger got, AtomicInteger notGot) {
            this.times = times;
            this.got = got;
            this.notGot = notGot;
        }

        public void run() {
            try {
                start.await(); //阻塞在这里，预备好了，准备开始信号就开始！
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (times > 0) {
                try {
//                    System.out.println(Thread.currentThread().getName()+" try to get connection");
                    Connection connection = pool.fetchConnection(1000);
                    if (connection == null) {
                        notGot.incrementAndGet();
//                        System.out.println(Thread.currentThread().getName()+" can not get connection");
                    } else {
//                        got.incrementAndGet();
//                        System.out.println(Thread.currentThread().getName()+" connection got, sleep a while");
//                        TimeUnit.MILLISECONDS.sleep(100); //假装用了一会儿;精髓在这里；一定要模拟出使用时间
//                        pool.releaseConnection(connection);
//                        System.out.println(Thread.currentThread().getName()+" connection released");
                        try{
                            connection.createStatement();
                            connection.commit();
                        }finally {
                            pool.releaseConnection(connection);
                            got.incrementAndGet();
                        }
                    }
                } catch (Exception e) {
                    //eat exception
                }finally {
                    times--;
                }
            }
            end.countDown();
        }
    }
}


