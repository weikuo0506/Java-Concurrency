package com.walker.concurrency.artof.chapter4base;

import java.util.concurrent.TimeUnit;

/**
 * Created by walker on 2017/1/29.
 */
public class Interrupted {
    public static void main(String[] args) throws InterruptedException {
        Thread sleep = new Thread(new SleepRunner(), "sleep-thread");

        Thread busy = new Thread(new BusyRunner(), "busy-thread");

//        sleep.setDaemon(true);
//        busy.setDaemon(true);

        sleep.start();
        busy.start();

        TimeUnit.SECONDS.sleep(5);

        //中断
        sleep.interrupt();
        busy.interrupt();
        TimeUnit.SECONDS.sleep(2);
        System.out.println("sleep thread is interrupted: " + sleep.isInterrupted());
        System.out.println("busy thread is interrupted: " + busy.isInterrupted());

        //再来一次试试
        sleep.interrupt();
        busy.interrupt();
        TimeUnit.SECONDS.sleep(2);
        System.out.println("sleep thread is interrupted: " + sleep.isInterrupted());
        System.out.println("busy thread is interrupted: " + busy.isInterrupted());

        TimeUnit.SECONDS.sleep(2);
        System.out.println("sleep thread is alive: " + sleep.isAlive());
        System.out.println("busy thread is alive: " + busy.isAlive());
        TimeUnit.SECONDS.sleep(2);


        System.out.println("all is over!");

    }

    private static class SleepRunner implements Runnable {
        public void run() {
            for(;;) {
                try {
                    TimeUnit.SECONDS.sleep(100);
                } catch (InterruptedException e) {
//                    e.printStackTrace();
                    System.out.println("Thread "+Thread.currentThread().getName() +" is intterrupted!,InterruptedException occur!");
                }
            }
        }
    }

    private static class BusyRunner implements Runnable {
        public void run() {
            for(;;) {
            }
        }
    }

}
