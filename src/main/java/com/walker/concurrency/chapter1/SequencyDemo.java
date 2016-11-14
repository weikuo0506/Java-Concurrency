package com.walker.concurrency.chapter1;

/**
 * @author walkerwei
 * @version 2016/11/14
 */
public class SequencyDemo {
    public static void main(String[] args) throws InterruptedException {
//        final UnsafeSequence sequency = new UnsafeSequence();
        final SafeSequency sequency = new SafeSequency();

        Thread t1 = new Thread(new Runnable() {
            public void run() {
                for(int i=0;i<100000;i++){
                    sequency.getNext();
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            public void run() {
                for(int i=0;i<100000;i++){
                    sequency.getNext();
                }
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();
        System.out.println(sequency.getNext());

    }
}
