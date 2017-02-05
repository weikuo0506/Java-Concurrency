package com.walker.concurrency.artof.chapter123jmm;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by walker on 2017/1/26.
 */
public class Counter2 {
    private int count1 = 0;
    private int count2 = 0;
    private AtomicInteger count3 = new AtomicInteger(0);

    private static final int threadSize = 1000;
    private static final int cycleTimes = 100000;

    public static void main(String[] args) {
        final Counter2 counter = new Counter2();
        List<Thread> threads = new ArrayList<Thread>();
        for(int i=0;i<threadSize;i++) {
            threads.add(new Thread(new Runnable() {
                public void run() {
                    for(int j=0;j<cycleTimes;j++) {
                        counter.count1();
                        counter.count2();
                        counter.count3();
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
        System.out.println("count1 = " + counter.count1);
        System.out.println("count2 = " + counter.count2);
        System.out.println("count3 = " + counter.count3.get());

    }

    private void count1() {
        count1++;
        count1--;
    }

    private synchronized void count2() {
        count2++;
        count2--;
    }

    private void count3() {
        for(;;) {
            int expect1 = count3.get();
            boolean suc1 = count3.compareAndSet(expect1, expect1 + 1); //但ABA问题呢
            if (suc1) {
                break;
            }
        }
        for(;;) {
            int expect2 = count3.get();
            boolean suc2 = count3.compareAndSet(expect2, expect2 - 1);
            if (suc2) {
                break;
            }
        }
    }
}
