package com.example.latch;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CountDownLatch {
    final Condition isZero;
    final Condition notZero;

    final Lock modifyValue;

    volatile int value;

    public CountDownLatch(int startingValue) {
        modifyValue = new ReentrantLock();
        isZero = new ReentrantLock().newCondition();
        notZero = new ReentrantLock().newCondition();
    }

    public void await() {

    }

    public void countDown() {

    }

    public void countUp() {

    }
}
