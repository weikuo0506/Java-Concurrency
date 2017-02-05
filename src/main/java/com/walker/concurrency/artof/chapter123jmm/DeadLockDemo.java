package com.walker.concurrency.artof.chapter123jmm;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by walker on 2017/1/26.
 */
public class DeadLockDemo {
    private static final String lockA = "a";
    private static final String lockB = "b";

    private final CountDownLatch latch = new CountDownLatch(2);

    public static void main(String[] args) {
        new DeadLockDemo().deadLock();
    }
    private void deadLock() {
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                synchronized (lockA) {
                    System.out.println("thread-1|lockA acquired,trying to get lockB");

                    try {
                        TimeUnit.SECONDS.sleep(1);  //加上一定发生死锁
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    synchronized (lockB) {
                        System.out.println("thread-1|lockB acquired");
                    }
                }
                System.out.println("thread-1 will be over!");
                latch.countDown();
            }
        });

        Thread t2 = new Thread(new Runnable() {
            public void run() {
                synchronized (lockB) {
                    System.out.println("thread-2|lockB acquired,trying to get lockA");
                    synchronized (lockA) {
                        System.out.println("thread-2|lockA acquired");
                    }
                }
                System.out.println("thread-2 will be over!");
                latch.countDown();
            }
        });

        t1.start();
        t2.start();

        try {
            latch.await();
            System.out.println("reach here means no deadLock happened!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
