package com.walker.concurrency.artof.chapter4base;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by walker on 2017/1/30.
 */
public class Join {
    public static void main(String[] args) throws InterruptedException {
        int threadSize = 10;
        List<Thread> threadList = new ArrayList<Thread>(threadSize);
        Thread previous = Thread.currentThread();
        for(int i=0;i<threadSize;i++) {
            Thread t = new Thread(new Domino(previous),"thread-"+i);
//            t.setDaemon(true);
            previous = t;
            threadList.add(t);
        }

        for (Thread t : threadList) {
            t.start();
        }
        TimeUnit.SECONDS.sleep(2);
        System.out.println(Thread.currentThread().getName()+" work over, and terminated!");

    }

    private static class Domino implements Runnable {
        private Thread previous;

        public Domino(Thread previous) {
            this.previous = previous;
        }

        public void run() {
            try {
                previous.join();  //等待前一个线程结束
                TimeUnit.SECONDS.sleep(1); //do something
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+" work over, and terminated!");
        }
    }

}
