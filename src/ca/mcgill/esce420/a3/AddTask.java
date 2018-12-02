package ca.mcgill.esce420.a3;

import java.util.concurrent.Future;

import static ca.mcgill.esce420.a3.Main.exec;

public class AddTask implements Runnable {
    Vector A, B, C;

    public AddTask (Vector a, Vector b, Vector c){
        A = a;
        B = b;
        C = c;
    }
    /*To do an addition we want to continue to split Vector A, B, and C in half recursively
     * until we reach a vector of size 1, then we add the reals together to produce the output
     * for C. We do this by assigning a future to each recursive split and add operation*/
    public void run(){
        try {
            int dim = A.getLength();
            if(dim == 1){
                C.set(0, A.get(0) + B.get(0));
            } else {
                Vector[] AA = A.split();
                Vector[] BB = B.split();
                Vector[] CC = C.split();

                Future<?>[] future = (Future<?>[]) new Future[2];
                for(int i = 0; i < 2; i++){
                    future[i] = exec.submit(new AddTask(AA[i],BB[i],CC[i]));

                }
                for(int i = 0; i < 2; i++){
                    future[i].get();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
