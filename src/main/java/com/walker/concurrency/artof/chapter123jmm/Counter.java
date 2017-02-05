package com.walker.concurrency.artof.chapter123jmm;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by walker on 2017/1/26.
 */
public class Counter {
    private int count1 = 0;
    private int count2 = 0;
    private AtomicInteger count3 = new AtomicInteger(0);

    private static final int threadSize = 100;
    private static final int cycleTimes = 10000;

    public static void main(String[] args) {
        final Counter counter = new Counter();
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
    }

    private synchronized void count2() {
        count2++;
    }

    private void count3() {
        for(;;) {
            int expect = count3.get();
            boolean suc = count3.compareAndSet(expect, ++expect); //CAS成功时候保证这两个操作是原子的；但ABA问题呢
            if (suc) {
                break;
            }
        }
    }
}
