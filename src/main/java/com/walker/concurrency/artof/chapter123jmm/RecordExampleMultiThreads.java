package com.walker.concurrency.artof.chapter123jmm;

/**
 * Created by walker on 2017/1/26.
 */
public class RecordExampleMultiThreads {
    int a = 0;
    boolean flag = false;
    int b = 4;

    private void write() {
        a = 1;
        flag = true;
    }

    private void read() {
        if (flag) {
            b = a*a;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final RecordExampleMultiThreads example = new RecordExampleMultiThreads();

        for(int i=0;i<100000;i++) {
            Thread w = new Thread(new Runnable() {
                public void run() {
                    example.write();
                }
            });

            Thread r = new Thread(new Runnable() {
                public void run() {
                    example.read();
                }
            });

            w.start();
            r.start();

            w.join();
            r.join();

            if (example.b != 4 && example.b !=1) {
                System.out.println(example.b);
            }

        }
    }
}
