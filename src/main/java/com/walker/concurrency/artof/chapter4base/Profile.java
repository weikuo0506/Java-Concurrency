package com.walker.concurrency.artof.chapter4base;

import java.util.concurrent.TimeUnit;

/**
 * Created by walker on 2017/1/30.
 */
public class Profile {
    private static ThreadLocal<Long> TIME_HOLDER = new ThreadLocal<Long>(){
        @Override
        protected Long initialValue() {
            return System.currentTimeMillis();
        }
    };
    public static void begin(){
        TIME_HOLDER.set(System.currentTimeMillis());
    }
    public static long timed(){
        return System.currentTimeMillis() - TIME_HOLDER.get();
    }


    public static void main(String[] args) throws InterruptedException {
        System.out.println("use profile for timing...");
        begin();
        TimeUnit.SECONDS.sleep(3);
        System.out.println("time cost: "+timed()+" ms");
    }
}
