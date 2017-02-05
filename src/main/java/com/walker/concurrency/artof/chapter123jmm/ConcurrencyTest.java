package com.walker.concurrency.artof.chapter123jmm;

import java.util.concurrent.*;

/**
 * Created by walker on 2017/1/24.
 */
public class ConcurrencyTest {
    private static long times = 10000;

    public static void main(String[] args) throws Exception {
        for(int i=0;i<6;i++) {
            serial(times);
            concurrency2(times);
            times *=10;
            System.out.println("---------------------");
        }


    }

    private static void serial(final long times){
        long start = System.currentTimeMillis();
        //task 1
        long a = 0;
        for(int i=0;i<times;i++) {
            a++;
        }
        //task 2
        long b = 0;
        for(int i=0;i<times;i++) {
            b--;
        }
        long time = System.currentTimeMillis()-start;
        System.out.println("serial|compute "+times+" over, a = "+a+",b = "+b+", time cost = "+ time+" ms.");
    }

    //runable是没有返回值的
    private static void concurrency1(final long times) throws InterruptedException {
        long start = System.currentTimeMillis();
        //task 1
        Thread t = new Thread(new Runnable() {
            public void run() {
                long a = 0;
                for(int i=0;i<times;i++) {
                    a++;
                };
            }
        });
        t.start();
        //task 2
        long b = 0;
        for(int i=0;i<times;i++) {
            b--;
        }
        t.join(); //wait for t to over
        long end = System.currentTimeMillis();
        System.out.println("concurrency1|compute "+times+"times over,b = "+b+", time cost = "+ (end-start)+" ms.");
    }

    //
    private static void concurrency2(final long times) throws Exception {
        long start = System.currentTimeMillis();
        //task 1
        Callable<Long> c = new Callable<Long>() {
            public Long call() throws Exception {
                long a = 0;
                for(int i=0;i<times;i++) {
                    a++;
                };
                return a;
            }
        };
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Long> future = executorService.submit(c);

//        Executors.callable(new Runnable() {
//            public void run() {
//                long a = 0;
//                for(int i=0;i<times;i++) {
//                    a++;
//                };
//            }
//        });
//
        //task 2
        long b = 0;
        for(int i=0;i<times;i++) {
            b--;
        }
        long a = future.get();
        long time = System.currentTimeMillis()-start;
        System.out.println("concurrency|compute "+times+" over, a = "+a+",b = "+b+", time cost = "+ time+" ms.");
        executorService.shutdown();//一定记得要关闭线程池
        if (!executorService.awaitTermination(2, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
        }
    }

}
