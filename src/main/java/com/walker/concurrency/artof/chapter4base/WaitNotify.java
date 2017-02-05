package com.walker.concurrency.artof.chapter4base;

import java.util.concurrent.TimeUnit;

/**
 * Created by walker on 2017/1/29.
 */
public class WaitNotify {
    private static final Object lock = new Object();
    private static volatile boolean on = false;

    public static void main(String[] args) throws InterruptedException {
        Thread wait = new Thread(new Wait(), "wait-thread");
        Thread notify = new Thread(new Notify(), "notify-thread");
        wait.start();
        TimeUnit.SECONDS.sleep(1);
        notify.start();

    }
    private static class Wait implements Runnable {
        public void run() {
            try {
                synchronized (lock) {
                    System.out.println(Thread.currentThread().getName()+" acquired chapter5lock");
                    TimeUnit.SECONDS.sleep(5); //模拟准备
                    int count = 0;
                    while (!on) {  //这里必须循环
                        ++count;
                        System.out.println(Thread.currentThread().getName()+" |on singnal is " + on + " ,wait forever! count = "+count);
                        lock.wait();
                        TimeUnit.SECONDS.sleep(2);
                        System.out.println(Thread.currentThread().getName()+" do other work,count = "+count);
                    }
                    System.out.println(Thread.currentThread().getName()+" |on is " + on + " now, begin to do work,count = "+count);
                    TimeUnit.SECONDS.sleep(5); //模拟干活
                    System.out.println(Thread.currentThread().getName()+" |work over!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static class Notify implements Runnable{
        public void run() {
            try {
                synchronized (lock){
                    System.out.println(Thread.currentThread().getName()+" acquired chapter5lock");
                    TimeUnit.SECONDS.sleep(10); //模拟准备
                    on = true;
                    System.out.println(Thread.currentThread().getName()+" |on is " + on + " now, begin to do notify");
                    lock.notify();
                    System.out.println(Thread.currentThread().getName()+" notity over");
                    TimeUnit.SECONDS.sleep(5); //模拟干活
                    System.out.println(Thread.currentThread().getName()+" work over!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
