package com.walker.concurrency.artof.chapter4base.threadpool;

import java.util.concurrent.TimeUnit;

/**
 * Created by walker on 2017/1/31.
 */
public class ThreadPoolTest {
    public static void main(String[] args) throws InterruptedException {
        ThreadPool<Job> threadPool = new DefaultThreadPool(5);
        Job job1 = new Job("job-1");
        threadPool.execute(job1);

        Job job2 = new Job("job-2");
        threadPool.execute(job2);

        TimeUnit.SECONDS.sleep(10);
        threadPool.shutdown();

        System.out.println("job size: "+threadPool.getJobSize());
    }
}
