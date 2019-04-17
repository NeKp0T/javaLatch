package com.example.latch;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CountDownLatchTest {

    class Task implements Runnable {
        private int delay;
        private AtomicInteger up;
        private AtomicInteger down;
        private CountDownLatch latch;

        Task(int delay, AtomicInteger up, AtomicInteger down, CountDownLatch latch) {
            this.delay = delay;
            this.up = up;
            this.down = down;
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(delay);
                latch.countDown();
                down.incrementAndGet();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void manyThreadsCountDownLatch() throws InterruptedException {

        var down = new AtomicInteger(0);
        var up = new AtomicInteger(0);
        int threadCounter = 10;

        CountDownLatch latch = new CountDownLatch(threadCounter);
        for (int i = 0; i < threadCounter; i++) {
            var worker = new Thread(new Task(i * 100, up, down, latch));
            worker.start();
        }

        // Main thread will wait until all thread finished
        latch.await();

        assertEquals(threadCounter, down.get());
    }

    @Test
    void manyThreadsCountUpLatch() throws InterruptedException {

        var down = new AtomicInteger(0);
        var up = new AtomicInteger(0);
        int threadCounter = 1;

        CountDownLatch latch = new CountDownLatch(0);
        var worker = new Thread(new Task(100, up, down, latch));
        worker.start();
        latch.countUp();
        latch.await();
        assertEquals(threadCounter, down.get());
    }
}