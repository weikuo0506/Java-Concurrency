package com.walker.concurrency.artof.chapter5lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Created by walker on 2017/1/31.
 * 同一时刻只允许最多两个线程同时访问，超过两个线程的访问将被阻塞；
 */
public class TwinsLock implements Lock {
    private final Sync sync = new Sync(2);
    public void lock() {
        sync.acquireShared(1);
    }

    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    public boolean tryLock() {
        int count = sync.tryAcquireShared(1);
        if (count >= 0) {
            return true;
        }else {
            return false;
        }
    }

    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireSharedNanos(1, unit.toNanos(time));
    }

    public void unlock() {
        sync.releaseShared(1);
    }

    public Condition newCondition() {
        return null;
    }

    /**
     * 利用静态内部类继承AQS，来委托实现锁机制
     * state含义：
     * 0：无锁可用，满锁，两把锁；
     * 1：已加了一把锁，还剩一把；
     * 2：无锁状态；还可以加两把锁；
     */
    private static final class Sync extends AbstractQueuedSynchronizer {
        public Sync(int count) {
            if (count < 0) {
                throw new IllegalArgumentException("count must larger than zero.");
            }
            setState(2);
        }

        /**
         *
         * @param reduceCount
         * @return
         */
        @Override
        protected int tryAcquireShared(int reduceCount) {
            for(;;) {
                int current = getState(); //旧值
                int newCount = current - reduceCount;  //可能的新值
                if (newCount < 0 || compareAndSetState(current, newCount)) {
                    return newCount; //结果<0,表示获取锁失败；=0，表示获取锁成功，但无剩余锁；>0表示获取锁成功，还有剩余锁；
                }
            }
        }

        @Override
        protected boolean tryReleaseShared(int returnCount) {
            for(;;) {
                int current = getState();
                int newCount = current + returnCount;
                if (compareAndSetState(current, newCount)) {
                    return true;  //看起来只能返回true啊
                }
            }
        }

    }

}
