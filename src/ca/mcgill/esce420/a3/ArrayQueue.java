package ca.mcgill.esce420.a3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ArrayQueue {

    private static int numThreads = 7;
    private static int INCREMENT = 0;

    public static void main(String args[]) {
        BoundedLockBasedQueue queue = new BoundedLockBasedQueue();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        for(int i = 0; i < numThreads; i++){
            executor.execute(new Incrementer(i, queue));
        }
    }

    public static class BoundedLockBasedQueue<T> {
        private final int size = 5;
        private Integer[] items = new Integer[size];
        private int headIndex = 0;
        private int tailIndex = 0;

        private final Lock enqLock = new ReentrantLock();
        private final Lock deqLock = new ReentrantLock();
        private final Condition notEmpty = deqLock.newCondition();
        private final Condition notFull = enqLock.newCondition();

        private boolean wakeUpDequeuers = false;
        private boolean wakeUpEnqueuers = false;

        public void enq(Integer item) throws InterruptedException{
            wakeUpDequeuers = false;
            enqLock.lock();
            try{
                while(tailIndex - headIndex >= size){
                    System.out.println("Queue full");
                    notFull.await();
                }
                items[tailIndex % items.length] = item;
                tailIndex++;
                if(tailIndex - headIndex == 1){
                    wakeUpDequeuers = true;
                }
            }finally{
                enqLock.unlock();
            }
            if(wakeUpDequeuers){
                deqLock.lock();
                try{
                    notEmpty.signalAll();
                }finally{
                    deqLock.unlock();
                }
            }
        }

        public void deq() throws InterruptedException{
            wakeUpEnqueuers = false;
            deqLock.lock();
            try{
                while(tailIndex - headIndex == 0){
                    System.out.println("Queue empty");
                    notEmpty.await();
                }
                items[headIndex % items.length] = null;
                headIndex++;
                if(tailIndex - headIndex == size - 1){
                    wakeUpEnqueuers = true;
                }
            }finally{
                deqLock.unlock();
            }
            if(wakeUpEnqueuers){
                enqLock.lock();
                try{
                    notFull.signalAll();
                }finally {
                    enqLock.unlock();
                }
            }
        }
    }

    /*
     * This incrementer will use the bakery lock to increment a common variable
     * */
    public static class Incrementer implements Runnable{
        int id;
        BoundedLockBasedQueue<Integer> queue;
        public Incrementer(int id, BoundedLockBasedQueue<Integer> queue){
            this.id = id;
            this.queue = queue;
        }

        @Override
        public void run() {
            for(int i = 0; i < 100; i++){
                // Enq with lock
                try {
                    Integer intI = i;
                    queue.enq(intI);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Wait random time
                INCREMENT++;
                System.out.println(INCREMENT);
                try {
                    Thread.sleep((long)(Math.random() * 1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Deq with lock
                try {
                    queue.deq();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
