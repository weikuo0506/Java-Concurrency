package com.walker.concurrency.artof.chapter123jmm;

/**
 * Created by walker on 2017/1/27.
 * 这种情况太难重现了，只是说有这种可能性
 */
public class FinalDemo {
    private int index = 0;
    private final int age;
    private static FinalDemo demo = null;

    public FinalDemo(int index, int age) {
        this.index = index;
        this.age = age;
//        demo = this;  //这就叫逸出
    }

    public static void main(String[] args) throws InterruptedException {
        Thread w = new Thread(new Runnable() {
            public void run() {
                demo = new FinalDemo(1,30);
            }
        });

        Thread r = new Thread(new Runnable() {
            public void run() {
                System.out.println("index: "+demo.index+" age:"+demo.age);
            }
        });

        w.start();
        r.start();
        w.join();
        r.join();
        System.out.println("over!");
    }
}
