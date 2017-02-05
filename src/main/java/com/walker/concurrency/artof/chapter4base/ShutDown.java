package com.walker.concurrency.artof.chapter4base;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by walker on 2017/1/29.
 */
public class ShutDown {
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static volatile boolean on = true;

    public static void main(String[] args) throws InterruptedException {
        //利用interrupt终止线程
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                for (; ; ) {
                    System.out.println(dateFormat.format(new Date()));
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        System.out.println("t1 is interrupted,prepare to stop.");
                        break; //必须跳出去才算结束
//                        return;
                    }
                }
            }
        });
        t1.start();
        TimeUnit.SECONDS.sleep(5);
        t1.interrupt();

        t1.join();
        System.out.println("t1 is over!");

        //利用标志位终止线程
        Thread t2 = new Thread(new Runnable() {
            public void run() {
                while(on) {
                    System.out.println(dateFormat.format(new Date()));
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t2.start();
        TimeUnit.SECONDS.sleep(5);
        on = false;

        t2.join();
        System.out.println("t2 is over!");

    }
}
