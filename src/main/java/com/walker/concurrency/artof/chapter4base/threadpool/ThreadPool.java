package com.walker.concurrency.artof.chapter4base.threadpool;

/**
 * Created by walker on 2017/1/31.
 * 参考下connectionPool的思想；固定5个连接，多个线程去获取连接；
 * 这里的job类似于连接，而多个worker线程去获取job，然后执行；
 * 与其说是线程池，不如说是工作池；
 */
public interface ThreadPool<Job> {
    void execute(Job job);  //执行任务

    void shutdown();        //关闭线程池

    void addWorkers(int num);

    void removeWorkers(int num);

    int getJobSize();
}
