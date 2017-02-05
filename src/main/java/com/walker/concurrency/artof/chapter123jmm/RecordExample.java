package com.walker.concurrency.artof.chapter123jmm;

/**
 * Created by walker on 2017/1/26.
 */
public class RecordExample {
    int a = 0;
    boolean flag = false;

    private void write() {
        a = 1;
        flag = true;
    }

    private void read() {
        if (flag) {
            System.out.println("a = " + a);
        }
    }

    public static void main(String[] args) {
        RecordExample example = new RecordExample();
        example.write();
        example.read();
    }
}
