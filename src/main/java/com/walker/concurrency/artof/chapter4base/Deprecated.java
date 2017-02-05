package com.walker.concurrency.artof.chapter4base;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by walker on 2017/1/29.
 */
public class Deprecated {
    //suspend resume stop
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(new Runnable() {
            public void run() {
                for(;;) {
                    System.out.println(dateFormat.format(new Date()));
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        t.start();
        TimeUnit.SECONDS.sleep(6);
        t.suspend();
        TimeUnit.SECONDS.sleep(2);
        t.resume();
        TimeUnit.SECONDS.sleep(4);
        t.stop();

    }


}
