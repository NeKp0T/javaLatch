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


        @Override
        public void run() {
            try {
                Thread.sleep(delay);
                down.incrementAndGet();
                latch.countDown();
                up.incrementAndGet();
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
            //var worker = new Throwable(new Task(i * 1000, latch, down, up, "WORKER" + i)));
            //worker.start();
        }

        // Main thread will wait until all thread finished
        latch.await();

        assertEquals(down, threadCounter);

        System.out.println(Thread.currentThread().getName() + " has finished");
    }

    @Test
    void manyThreadsCountUpLatch() throws InterruptedException {

        var down = new AtomicInteger(0);
        var up = new AtomicInteger(0);
        int threadCounter = 10;

        CountDownLatch latch = new CountDownLatch(threadCounter + 1);
        for (int i = 0; i < threadCounter; i++) {
           // Worker worker = new Worker(i * 1000, latch, down, up, "WORKER" + i);
            //worker.start();
        }


        // Main thread will wait until all thread finished
        latch.await();

        assertEquals(down, threadCounter);

        System.out.println(Thread.currentThread().getName() + " has finished");

    }
}