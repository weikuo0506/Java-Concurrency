package com.walker.concurrency.artof.chapter123jmm;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by walker on 2017/1/26.
 */
public class DeadLockDemoImprove {
    private static final Lock lockA = new ReentrantLock();
    private static final Lock lockB = new ReentrantLock();

    private final CountDownLatch latch = new CountDownLatch(2);

    public static void main(String[] args) {
        new DeadLockDemoImprove().deadLock();
    }
    private void deadLock() {
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                try {
                    if (lockA.tryLock(2,TimeUnit.SECONDS)) {
                        try{
                            System.out.println("thread-1|lockA acquired,trying to get lockB");

                            try {
                                TimeUnit.SECONDS.sleep(1);  //加上一定发生死锁
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            if (lockB.tryLock(1, TimeUnit.SECONDS)) {
                                try{
                                    System.out.println("thread-1|lockB acquired");
                                }finally {
                                    lockB.unlock();
                                }
                            }
                        }finally {
                            lockA.unlock();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("thread-1 will be over!");
                latch.countDown();
            }
        });

        Thread t2 = new Thread(new Runnable() {
            public void run() {
                try {
                    if (lockB.tryLock(2, TimeUnit.SECONDS)) {
                        try{
                            System.out.println("thread-2|lockB acquired,trying to get lockA");
                            if (lockA.tryLock(1, TimeUnit.SECONDS)) {
                                try{
                                    System.out.println("thread-2|lockA acquired");
                                }finally {
                                    lockA.unlock();
                                }
                            }
                        }finally {
                            lockB.unlock();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
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
