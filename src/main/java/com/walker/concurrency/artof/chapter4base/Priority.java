package com.walker.concurrency.artof.chapter4base;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by walker on 2017/1/29.
 */
public class Priority {
    private static volatile boolean start = false;
    private static volatile boolean end = false;

    public static void main(String[] args) throws InterruptedException {

        List<Job> jobs = new ArrayList<Job>();
        for (int priority = 1; priority <= 10; priority++) {
            Job job = new Job();
            jobs.add(job);
            Thread t = new Thread(job);
            t.setPriority(priority);
            t.start();
        }
        start = true;  //start!
        TimeUnit.SECONDS.sleep(10);
        end = true;   //end!
        for (Job job : jobs) {
            System.out.println("job count: "+job.runCount);  //还是有差别的
        }
    }

    private static class Job implements Runnable {
        private long runCount = 0;
        public void run() {
            while (!start) {
                Thread.yield();
            }
            while (!end) {
                ++runCount;
                Thread.yield();
            }
        }
    }

}
