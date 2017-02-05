package com.walker.concurrency.artof.chapter4base;

import java.util.concurrent.TimeUnit;

/**
 * Created by walker on 2017/1/29.
 */
public class Daemon {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    System.out.println("this is finally block, only for test!");
                }
            }
        });
        t.setDaemon(true);
        t.start();
//        t.join();
        System.out.println("over");
    }
}
