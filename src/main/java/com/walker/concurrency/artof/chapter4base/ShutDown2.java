package com.walker.concurrency.artof.chapter4base;

import java.util.concurrent.TimeUnit;

/**
 * Created by walker on 2017/1/29.
 */
public class ShutDown2 {
    public static void main(String[] args) throws InterruptedException {
        //利用interrupt终止线程
        Counter counter1 = new Counter();
        Thread t1 = new Thread(counter1,"counter1");
        t1.start();
        TimeUnit.SECONDS.sleep(5);
        t1.interrupt();  //中断

        t1.join();
        System.out.println("t1 is over! count = "+counter1.getCount());

        //利用标志位终止线程
        Counter counter2 = new Counter();
        Thread t2 = new Thread(counter2,"counter2");
        t2.start();
        TimeUnit.SECONDS.sleep(5);
        counter2.cancel();  //更改继续标识

        t2.join();
        System.out.println("t2 is over! count = "+counter2.getCount());

    }


    private static class Counter implements Runnable{
        private volatile boolean on = true;
        private int count = 0;
        public void run() {
            while (on && !Thread.currentThread().isInterrupted()) {
                ++count;
            }
        }
        //用于发终止信号
        public void cancel(){
            System.out.println("on signal send from "+Thread.currentThread().getName());
            on = false;
        }
        public int getCount() {
            return count;
        }
    }
}
