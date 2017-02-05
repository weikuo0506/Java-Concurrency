package com.walker.concurrency.artof.chapter5lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Created by walker on 2017/1/31.
 * 同一时刻只允许最多两个线程同时访问，超过两个线程的访问将被阻塞；
 */
public class TwinsLockTest{
    private static final Lock lock = new TwinsLock();
    public static void main(String[] args) throws InterruptedException {
        for(int i=0;i<10;i++) {
            new Thread(new Worker(), "Thread-" + i).start();
        }
        while (true) {
            TimeUnit.MILLISECONDS.sleep(500);
            System.out.println();
        }
    }


    static class Worker implements Runnable {

        public void run() {
            while (true) {
                lock.lock();
                try{
                    System.out.println(Thread.currentThread().getName()+ "got lock!");
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    lock.unlock();
                }
            }
        }
    }
}
