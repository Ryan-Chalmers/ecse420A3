package ca.mcgill.esce420.a3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static ca.mcgill.esce420.a3.Main.exec;

public class MultTask implements Runnable {
    Matrix A;
    Vector B, C, lhs, rhs;



    public MultTask(Matrix a, Vector b, Vector c){
        A=a; B=b; C=c;
        lhs = new Vector(b.length);
        rhs = new Vector(b.length);

    }

    public void run(){
        try{
            if(A.getDim() == 1){
                C.set(0, A.get(0,0)*B.get(0));
            } else {
                Matrix AA[][] = A.split();
                Vector BB[] = B.split();
                Vector CC[] = C.split();
                Vector LL[] = lhs.split();
                Vector RR[] = rhs.split();

                Future<?> [][] future = (Future<?>[][]) new Future[2][2];

                for(int i = 0; i < 2; i++){
                    future[i][0] = exec.submit(new MultTask(AA[i][0], BB[0], LL[i]));
                    future[i][1] = exec.submit(new MultTask(AA[1][i], BB[1], RR[i]));
                }

                for(int i = 0; i < 2; i++){
                    for(int j = 0; j < 2; j ++){
                        future[i][j].get();
                    }
                }

                Future<?> done = exec.submit(new AddTask(lhs, rhs, C));
                done.get();

            }


        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
