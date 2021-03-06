package com.walker.concurrency.artof.chapter123jmm;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by walker on 2017/1/27.
 */
public class SingletonDoubleCheckVolatile {
    private static volatile SingletonDoubleCheckVolatile instance;

    private SingletonDoubleCheckVolatile() { //关闭创建入口，防止其他程序自己new
        System.out.println("new instance.");
    }

    public static SingletonDoubleCheckVolatile getInstance() {
        if (instance == null) {
            synchronized (SingletonDoubleCheckVolatile.class) {
                if (instance == null) {
                    instance = new SingletonDoubleCheckVolatile();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) throws InterruptedException {
        final int threadSize = 10000;
        List<Thread> threads = new ArrayList<Thread>();
        final CountDownLatch latch = new CountDownLatch(threadSize);
        for(int i=0;i<threadSize;i++) {
            Thread t = new Thread(new Runnable() {
                public void run() {
                    getInstance();
                    latch.countDown();
                }
            });
            threads.add(t);
        }

        long start = System.currentTimeMillis();
        for (Thread t : threads) {
            t.start();  //几乎同时启动
        }
        latch.await();
        System.out.println("over,time cost = "+ (System.currentTimeMillis()-start)+" ms");
    }
}

