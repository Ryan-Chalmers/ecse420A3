package ca.mcgill.esce420.a3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ArrayQueueNoLock {

    private static int numThreads = 4;
    private static int INCREMENT = 0;

    /**
     * This main() method will continuously run enq and deq calls with multiple threads
     * */
    public static void main(String args[]) {
        BoundedQueue queue = new BoundedQueue();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        for(int i = 0; i < numThreads; i++){
            executor.execute(new Incrementer(i, queue));
        }
    }

    /**
     * This BoundedQueue class is an object without internal mutual attributes
     * */
    public static class BoundedQueue<T> {

        // Set queue at a length with head and tail together
        private final int size = 3;
        private AtomicInteger[] items = new AtomicInteger[size];
        private int headIndex = 0;
        private int tailIndex = 0;

        // Set pseudo lock will be a shared boolean representing if another thread is using the queue
        private boolean isShared;

        // Similar to lock based algorithms, only enq if the flag is down and set a flag during CS
        public void enq(AtomicInteger item){
            while(tailIndex - headIndex >= size){
                System.out.println("Queue full");
            }
            while(isShared);
            isShared = true;
            items[tailIndex % items.length] = item;
            tailIndex++;
            isShared = false;
        }

        // Similar to lock based algorithms, only dew if the flag is down and set a flag during CS
        public void deq(){
            while(tailIndex - headIndex == 0){
                System.out.println("Queue empty");
            }
            while(isShared);
            isShared = true;
            items[headIndex % items.length] = null;
            headIndex++;
            isShared = false;
        }
    }

    /*
     * This incrementer will use the bakery lock to increment a common variable
     * */
    public static class Incrementer implements Runnable{
        int id;
        BoundedQueue<AtomicInteger> queue;
        public Incrementer(int id, BoundedQueue<AtomicInteger> queue){
            this.id = id;
            this.queue = queue;
        }

        @Override
        public void run() {
            for(int i = 0; i < 10; i++){
                // Enq without lock
                AtomicInteger intI = new AtomicInteger(i);
                queue.enq(intI);

                // Wait random time
                INCREMENT++;
                System.out.println(INCREMENT);
                try {
                    Thread.sleep((long)(Math.random() * 1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Deq without lock
                queue.deq();
            }
        }
    }
}
