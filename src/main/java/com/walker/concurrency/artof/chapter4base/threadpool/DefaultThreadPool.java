package com.walker.concurrency.artof.chapter4base.threadpool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by walker on 2017/1/31.
 * 这里的threadPool其实指的是workerPool
 */
public class DefaultThreadPool<Job extends Runnable> implements ThreadPool<Job> {

    private static final int MIN_WORKER_NUMS = 1;
    private static final int MAX_WORKER_NUMS = 10;
    private static final int DEFAULT_WORKER_NUMS = 5;  //线程池大小就是工作者线程数目；


    private final LinkedList<Job> jobs = new LinkedList<Job>(); //这是job池；
    private final List<Worker> workers = Collections.synchronizedList(new ArrayList<Worker>());

    private AtomicLong threadId = new AtomicLong();

    public DefaultThreadPool() {
        addWorkers(DEFAULT_WORKER_NUMS);
    }

    public DefaultThreadPool(int threadPoolSize) {
        threadPoolSize = threadPoolSize > MAX_WORKER_NUMS ? MAX_WORKER_NUMS : threadPoolSize < MIN_WORKER_NUMS ? MIN_WORKER_NUMS : threadPoolSize;
        addWorkers(threadPoolSize);
    }
    /**
     * 执行一个任务，并不是真的执行，只是提交到job池就可以了；worker线程会不断获取；
     * @param job
     */
    public void execute(Job job) {
        if(job == null) return;
        synchronized (jobs) {
            jobs.addLast(job);
            jobs.notify(); //唤醒一个就可以了
            System.out.println(job + " added to pool,and notified all workers");
        }
    }

    public void shutdown() {
        for (Worker worker : workers) {
            worker.shutdown();
            System.out.println("worker has been shutdown");
        }
    }

    public void addWorkers(int num) {
        int currSize = workers.size();
        num = num > (MAX_WORKER_NUMS - currSize) ? (MAX_WORKER_NUMS - currSize) : num;
        for(int i=0;i<num;i++) {
            final Worker worker = new Worker();
            workers.add(worker);
            System.out.println("worker added to pool and will start now");
            new Thread(worker,"ThreadPool-Worker-"+threadId.incrementAndGet()).start(); //启动worker，即创建加入后就开始不断查询判断执行。
        }
    }

    public void removeWorkers(int num) {
        synchronized (jobs) {  //为什么需要对job加锁呢？
            int currSize = workers.size();
            num = num > (currSize - MIN_WORKER_NUMS) ? (currSize - MIN_WORKER_NUMS) : num;
            for(int i=0;i<num;i++) {
                System.out.println("remove worker");
                Worker worker = workers.get(i);
                if (workers.remove(worker)) {//从worker池中删除
                    worker.shutdown();        //停止worker
                }
            }
        }
    }

    public int getJobSize() {
        return jobs.size();
    }

    /**
     * 搞成内部类，便于访问外部类变量啊！！！
     */
    private class Worker implements Runnable {
        private volatile boolean running = true;

        public void run() {
            while (running) {
                synchronized (jobs){ //尽量缩小锁的范围
                    while (jobs.isEmpty()) {
                        if(!running)return; //二次检查很必要！
                        try {
                            jobs.wait(1000);   //这里之前有隐含bug?；如果jobs为空，会一直阻塞在这里，导致无法shutdown；
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if(!running)return;
                    System.out.println("having job! will get and do it!");
                    Job job = jobs.removeFirst();
                    if(job == null) return;
                    job.run(); //任务执行完成一次了！循环，执行下一次；
                }
            }
        }

        public void shutdown() {
            running = false;
        }
    }

}
