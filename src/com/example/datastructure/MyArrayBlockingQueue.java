package com.example.datastructure;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MyArrayBlockingQueue {

    private final ArrayList<Integer> a;
    private final int capacity;
    /** Main lock guarding all access */
    private final ReentrantLock lock;
    /** Condition for waiting takes */
    private final Condition notEmpty;
    /** Condition for waiting puts */
    private final Condition notFull;

    public MyArrayBlockingQueue(int capacity){
        this.capacity = capacity;
        this.a = new ArrayList<Integer>(capacity);
        this.lock = new ReentrantLock();
        this.notEmpty = lock.newCondition();
        this.notFull = lock.newCondition();
    }

    public void put(Integer i) throws InterruptedException {
        lock.lockInterruptibly();
        try {
            while (a.size() == capacity)
                notFull.await();//release monitor and sleep until notFull.signal is called

            a.add(i);
            notEmpty.signal();// wake up one random thread in take() method
        } finally {
            lock.unlock();
        }
    }

    public void take() throws InterruptedException {
        lock.lockInterruptibly();
        try {
            while (a.isEmpty())
                notEmpty.await();//realease monitor and sleep until notEmpty.signal is called

            a.remove(0);
            notFull.signal();// wake up one random thread in put() method
        } finally {
            lock.unlock();
        }
    }

}
