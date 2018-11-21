package ca.mcgill.esce420.a3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ArrayQueueNoLock {

    private static int numThreads = 2;
    private static int INCREMENT = 0;

    public static void main(String args[]) {
        BoundedLockBasedQueue queue = new BoundedLockBasedQueue();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        for(int i = 0; i < numThreads; i++){
            executor.execute(new Incrementer(i, queue));
        }
    }

    public static class BoundedLockBasedQueue<T> {
        private final int size = 2;
        private Integer[] items = new Integer[size];
        private int headIndex = 0;
        private int tailIndex = 0;
        private boolean isShared;

        public void enq(Integer item){
            while(tailIndex - headIndex >= size){
                System.out.println("Queue full");
            }
            while(isShared);
            isShared = true;
            items[tailIndex % items.length] = item;
            tailIndex++;
            isShared = false;
        }

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
        BoundedLockBasedQueue<Integer> queue;
        public Incrementer(int id, BoundedLockBasedQueue<Integer> queue){
            this.id = id;
            this.queue = queue;
        }

        @Override
        public void run() {
            for(int i = 0; i < 100; i++){
                // Enq without lock
                Integer intI = i;
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
