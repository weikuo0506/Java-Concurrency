package com.walker.concurrency.artof.chapter4base;

import java.util.concurrent.TimeUnit;

/**
 * Created by walker on 2017/1/29.
 */
public class ThreadState {
    //展示waiting，time_waiting，blocked等三个状态；
    public static void main(String[] args) {
        new Thread(new TimeWaitingDemo(),"time-waiting-demo").start();
        new Thread(new WaitingDemo(),"waiting-demo").start();
        new Thread(new BlockedDemo(),"blocked-demo-1").start();
        new Thread(new BlockedDemo(),"blocked-demo-2").start();
    }


    private static class TimeWaitingDemo implements Runnable {
        public void run() {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(100); //不断睡眠，只要指定时间睡眠，那就是Time_waiting!要么等待通知、中断，要么自行超时；
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class WaitingDemo implements Runnable {
        public void run() {
            while (true) {
                synchronized (WaitingDemo.class) {
                    try {
                        WaitingDemo.class.wait(); //只能等待通知中断；
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static class BlockedDemo implements Runnable {
        public void run() {
            synchronized (BlockedDemo.class) {
                while (true) {
                    try {
                        TimeUnit.SECONDS.sleep(100); //获得锁了就睡眠，必然导致另一个线程阻塞与该锁；
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

