package com.walker.concurrency.artof.chapter4base.connpool;

import java.sql.Connection;
import java.util.LinkedList;

/**
 * Created by walker on 2017/1/30.
 */
public class ConnectionPool {
    private LinkedList<Connection> pool = new LinkedList<Connection>();

    public ConnectionPool(int initialSize) {
        for(int i=0;i<initialSize;i++) {
            pool.add(ConnectionDriver.createConnection());
        }
    }


    public void releaseConnection(Connection connection) {
        if (connection == null) {
            return;
        }
        synchronized (pool) {
            pool.addLast(connection); //加在最后，等同于add();
            pool.notifyAll();         //释放后通知，便于消费者及时取到
        }
    }

    public Connection fetchConnection(long timeout) throws InterruptedException {
        synchronized (pool) {
            if (timeout <= 0) {  //立即判断，无限等待？我觉得应该是立即返回！
                if (pool.isEmpty()) {
                    return null;
                } else {
                    return pool.removeFirst(); //必须取出并删除
                }
            } else {
                final long future = System.currentTimeMillis()+timeout;
                while (pool.isEmpty() && timeout > 0) {
                    pool.wait(timeout);
                    timeout = future - System.currentTimeMillis();
                }
                if (pool.isEmpty()) {
                    return null;
                } else {
                    return pool.removeFirst();  //必须取出来！
                }
            }
        }
    }
}


