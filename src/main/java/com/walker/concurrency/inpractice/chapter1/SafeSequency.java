package com.walker.concurrency.inpractice.chapter1;

/**
 * @author walkerwei
 * @version 2016/11/14
 */
public class SafeSequency {
    private int value;
    public synchronized int getNext(){
        return value ++;
    }
}
