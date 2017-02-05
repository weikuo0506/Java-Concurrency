package com.walker.concurrency.artof.chapter4base.threadpool;

import java.util.concurrent.TimeUnit;

/**
 * Created by walker on 2017/1/31.
 */
public class Job implements Runnable{
    private String name;

    public Job(String name) {
        this.name = name;
    }

    public void run() {
        try {
            TimeUnit.SECONDS.sleep(1); //模拟执行任务过程
            System.out.println("jobName: "+name+" is doing work!");
            TimeUnit.SECONDS.sleep(2); //模拟执行任务过程
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String toString() {
        return "Job{" +
                "name='" + name + '\'' +
                '}';
    }
}
