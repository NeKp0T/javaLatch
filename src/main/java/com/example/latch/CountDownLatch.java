package com.example.latch;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// TODO try finally
public class CountDownLatch {
    private final Lock changesZeroeness;
    private final Condition becomesPositive;
    private final Lock becomesZeroLock;
    private final Condition becomesZero;

    private final Lock modifyValue;

    private int value;

    public CountDownLatch(int startingValue) {
        modifyValue = new ReentrantLock();

        changesZeroeness = new ReentrantLock();
        becomesZeroLock = new ReentrantLock();
        becomesPositive = changesZeroeness.newCondition();
        becomesZero = changesZeroeness.newCondition();

        modifyValue.lock();
        value = startingValue;
        modifyValue.unlock();
    }

    public void await() throws InterruptedException {
        becomesZeroLock.lock();
        if (value == 0) {
            becomesZeroLock.unlock();
            return;
        }

        becomesZero.wait();
        becomesZeroLock.unlock();
    }

    public void countDown() throws InterruptedException {
        changesZeroeness.lock();
        while (value == 0) {
            becomesPositive.wait();
        }
        // now value > 0

        modifyValue.lock();

        value--;
        if (value == 0) {
            becomesZero.notifyAll();
        }

        modifyValue.unlock();

        changesZeroeness.unlock();
    }

    public void countUp() {
        modifyValue.lock();
        if (value == 0) {
            changesZeroeness.lock(); // only if value == 0
            value++;
            becomesPositive.notify();
            changesZeroeness.unlock();
        } else {
            value++;
        }

        modifyValue.unlock();
    }
}
