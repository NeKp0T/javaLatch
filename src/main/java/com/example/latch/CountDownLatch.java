package com.example.latch;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// TODO try finally
public class CountDownLatch {
    private final Lock changesZeroeness;
    private final Condition becomesPositive;
    private final Condition becomesZero;

    private final Lock modifyValue;

    private int value;

    public CountDownLatch(int startingValue) {
        modifyValue = new ReentrantLock();

        changesZeroeness = new ReentrantLock();
        becomesPositive = changesZeroeness.newCondition();
        becomesZero = changesZeroeness.newCondition();

        modifyValue.lock();
        value = startingValue;
        modifyValue.unlock();
    }

    public void await() throws InterruptedException {
        changesZeroeness.lock();
        try {
            if (value == 0) {
                changesZeroeness.unlock();
                return;
            }

            becomesZero.await();
        } finally {
            changesZeroeness.unlock();
        }
    }

    public void countDown() throws InterruptedException {
        changesZeroeness.lock();
        try {
            while (value == 0) {
                becomesPositive.await();
            }
            // now value > 0

            modifyValue.lock();

            value--;
            if (value == 0) {
                becomesZero.signalAll();
            }

            modifyValue.unlock();
        } finally {
            changesZeroeness.unlock();
        }
    }

    public void countUp() {
        modifyValue.lock();
        if (value == 0) {
            changesZeroeness.lock(); // only if value == 0
            value++;
            becomesPositive.signal();
            changesZeroeness.unlock();
        } else {
            value++;
        }

        modifyValue.unlock();
    }
}
