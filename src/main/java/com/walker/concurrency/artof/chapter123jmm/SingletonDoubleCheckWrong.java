package com.walker.concurrency.artof.chapter123jmm;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by walker on 2017/1/27.
 */
public class SingletonDoubleCheckWrong {
    private int age = 0;
    private static SingletonDoubleCheckWrong instance;

    private SingletonDoubleCheckWrong() { //关闭创建入口，防止其他程序自己new
        age = 30;
        System.out.println("new instance.");
    }

    public static SingletonDoubleCheckWrong getInstance() {
        if (instance == null) {
            synchronized (SingletonDoubleCheckWrong.class) {
                if (instance == null) {
                    instance = new SingletonDoubleCheckWrong();
                }
            }
        }
        //这个错误原因比较隐晦，是说instance虽然！=null，但可能初始化没完成，
        //别的线程看到的可能是未完成初始化的instance
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

