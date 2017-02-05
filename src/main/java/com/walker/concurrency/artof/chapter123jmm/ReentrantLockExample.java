package com.walker.concurrency.artof.chapter123jmm;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by walker on 2017/1/26.
 */
public class ReentrantLockExample {
    int a = 0;
    ReentrantLock lock = new ReentrantLock();
    private void write() {
        lock.lock();
        try{
            a = 100; //a++只能用lock才能做到
        }finally {
            lock.unlock();
        }
    }

    private void read() {
        lock.lock();
        try{
            int tmp = a;
        }finally {
            lock.unlock();
        }
    }

    //对比volatile
    volatile int b = 0;
    private void write2() {
        b = 100;
    }
    private void read2() {
        int tmp = b;
    }
}
