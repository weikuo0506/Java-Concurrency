package com.walker.concurrency.artof.chapter123jmm;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by walker on 2017/1/26.
 */
public class Counter3 {
    private int count1 = 0;
    private int count2 = 0;
    private AtomicInteger count3 = new AtomicInteger(0);

    private static final int threadSize = 100;
    private static final int cycleTimes = 10000;
    private static final int plusThreads = 50;

    public static void main(String[] args) {
        final Counter3 counter = new Counter3();
        List<Thread> threads = new ArrayList<Thread>(threadSize);
        for(int i=0;i<plusThreads;i++) {
            threads.add(new Thread(new Runnable() {
                public void run() {
                    for(int j=0;j<cycleTimes;j++) {
                        counter.plus1();
                        counter.plus2();
                        counter.plus3();
                    }
                }
            }));
        }

        for(int i=0;i<threadSize-plusThreads;i++) {
            threads.add(new Thread(new Runnable() {
                public void run() {
                    for(int j=0;j<cycleTimes;j++) {
                        counter.minus1();
                        counter.minus2();
                        counter.minus3();
                    }
                }
            }));
        }

        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            try {
                t.join(); //等待所有线程结束，也可以用CountDownLatch
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("all threads over!");
        System.out.println("plus1 = " + counter.count1);
        System.out.println("plus2 = " + counter.count2);
        System.out.println("plus3 = " + counter.count3.get());

    }

    private void plus1() {
        count1++;
    }

    private void minus1() {
        count1--;
    }

    private synchronized void plus2() {
        count2++;
    }

    private synchronized void minus2() {
        count2--;
    }

    private void plus3() {
        for(;;) {
            int expect1 = count3.get();
            boolean suc1 = count3.compareAndSet(expect1, expect1 + 1); //但ABA问题呢：ABA问题只会出现在objcet的引用上，这里没问题
            if (suc1) {
                break;
            }
        }
    }

    private void minus3() {
        for(;;) {
            int expect2 = count3.get();
            boolean suc2 = count3.compareAndSet(expect2, expect2 - 1);
            if (suc2) {
                break;
            }
        }
    }
}
