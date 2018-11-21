package ca.mcgill.esce420.a3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ContainsCode {

    private static int numThreads = 5;

    public static void main(String args[]) {
        Node third = new Node(1, null);
        Node second = new Node(2, third);
        Node first = new Node(3, second);

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        for(int i = 0; i < numThreads; i++){
            executor.execute(new Contains(i, first));
        }
    }

    public static class Node {
        private Lock nodeLock = new ReentrantLock();
        private Integer value = null;
        private Node next = null;

        public Node(Integer value, Node next) {
            this.value = value;
            this.next = next;
        }

        public Node getNext(){
            nodeLock.lock();
            Node nextNode = this.next;
            nodeLock.unlock();
            return nextNode;
        }

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

        @Override
        public void run() {
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
