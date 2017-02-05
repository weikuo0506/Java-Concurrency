package com.walker.concurrency.artof.chapter5lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by walker on 2017/1/31.
 */
public class LockUseCase {
    public static void main(String[] args) {

    }

    private void lockDemo(){
        Lock lock = new ReentrantLock();
        lock.lock();
        try{
            //doSomething;
        }finally {
            lock.unlock();
        }
    }

    private void doSomething(){
        System.out.println("do something");
    }
}
