package ca.mcgill.esce420.a3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ContainsCode {

    private static int numThreads = 5;

    /**
     * This main() method will test the fine grained contains() algorithm
     * First, link 3 nodes in a list where their values are stored in increasing index
     * Second, run contains on a superset of the values stores
     * Note: The threads run in random order using sleep()
     * */
    public static void main(String args[]) {
        Node third = new Node(3, null);
        Node second = new Node(2, third);
        Node first = new Node(1, second);

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        for(int i = 0; i < numThreads; i++){
            executor.execute(new Contains(i, first));
        }
    }

    /**
     * This Node object contains a value and points to another Node
     * They also have a lock which makes their lists fine grained
     */
    public static class Node {
        private Lock nodeLock = new ReentrantLock();
        private Integer value = null;
        private Node next = null;

        public Node(Integer value, Node next) {
            this.value = value;
            this.next = next;
        }

        /**
         * Get next will lock and return the next node
         */
        public Node getNext(){
            nodeLock.lock();
            Node nextNode = this.next;
            nodeLock.unlock();
            return nextNode;
        }

        /**
         * Get value will lock and return the node value
         */
        public Integer getValue(){
            nodeLock.lock();
            Integer val = this.value;
            nodeLock.unlock();
            return val;
        }
    }

    public static class Contains implements Runnable{
        Integer value;
        Node node;

        public Contains(int value, Node node){
            this.value = value;
            this.node = node;
        }

        /**
         * This is the fine grained contains() algorithm from class
         */
        @Override
        public void run() {
            // Sleep for a random time to shuffle thread order and further prove correctness
            try {
                Thread.sleep((long)(Math.random() * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int index = 0;
            boolean isFound = false;

            while(node != null){
                if(node.getValue() == value){
                    isFound = true;
                    System.out.println("Value " + value + " found at index: " + index);
                    break;
                }else{
                    index++;
                }
                node = node.getNext();
            }
            if(!isFound){
                System.out.println("Value " + value + " is not here!");
            }
        }
    }
}
