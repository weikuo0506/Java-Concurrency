package com.walker.concurrency.chapter1;

/**
 * @author walkerwei
 * @version 2016/11/14
 */

public class UnsafeSequence {
    private int value;
    public int getNext(){
        return value++;
    }
}
