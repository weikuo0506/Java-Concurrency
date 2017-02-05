package com.walker.concurrency.artof.chapter123jmm;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by walker on 2017/1/27.
 */
public class SingletonEagerLoad {

    private static final SingletonEagerLoad instance = new SingletonEagerLoad(); //类加载的时候就初始化

    private SingletonEagerLoad() { //关闭创建入口，防止其他程序自己new
        System.out.println("new instance.");
    }

    public static SingletonEagerLoad getInstance() {
        return instance;
    }

    public static void main(String[] args) throws InterruptedException {
        final int threadSize = 100000;  //可以看到新建了多次,偶然出现
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

