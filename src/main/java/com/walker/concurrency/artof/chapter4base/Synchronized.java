package com.walker.concurrency.artof.chapter4base;

/**
 * Created by walker on 2017/1/29.
 */
public class Synchronized {

    public static void main(String[] args) {
        //对class对象加锁
        synchronized (Synchronized.class) {

        }
        //静态同步方法
        test();
    }
    public static synchronized void test() {

    }
}
