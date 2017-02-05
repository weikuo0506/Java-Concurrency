package com.walker.concurrency.artof.chapter123jmm;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * Created by walker on 2017/1/26.
 */
public class ABA {

    private static AtomicInteger atomicInt = new AtomicInteger(100);
    private static AtomicStampedReference<Integer> atomicStampedRef = new AtomicStampedReference<Integer>(100, 0);

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                atomicInt.compareAndSet(100, 101);
                atomicInt.compareAndSet(101, 100);
            }
        });

        Thread t2 = new Thread(new Runnable() {
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                boolean suc = atomicInt.compareAndSet(100, 101);
                if (suc) {
                    System.out.println("AtomicInteger update success!");
                }
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        Thread refT1 = new Thread(new Runnable() {
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                atomicStampedRef.compareAndSet(100, 101,
                        atomicStampedRef.getStamp(), atomicStampedRef.getStamp()+1);
                atomicStampedRef.compareAndSet(101, 100,
                        atomicStampedRef.getStamp(), atomicStampedRef.getStamp()+1);
            }
        });

        Thread refT2 = new Thread(new Runnable() {
            public void run() {
                int value = atomicStampedRef.getReference();
                int stamp = atomicStampedRef.getStamp();
                System.out.println("before sleep : value = "+value+" stamp = " + stamp);    // stamp = 0
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("after sleep : value = "+atomicStampedRef.getReference()+" stamp = " + atomicStampedRef.getStamp());//stamp = 1
                boolean suc = atomicStampedRef.compareAndSet(100, 101, stamp, stamp+1);
                if (!suc) {
                    System.out.println("AtomicStampedReference Integer update fail!");
                }
            }
        });

        refT1.start();
        refT2.start();
    }

}
