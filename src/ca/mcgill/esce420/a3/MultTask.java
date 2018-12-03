package ca.mcgill.esce420.a3;

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
            //If the dimension of the matrix is less than 250 we want to begin sequentially multiplying the matrix. This
            //is to prevent the algorithm from creating more threads than the system can handle
            if(A.getDim() <= 250){
               C = Main.sequentialMult(A, B);
            } else {

                /*Here we are recursively splitting the input matrix, and vector into smaller matrices, allowing us to
                 * perform multiple matrix multiplications, as well as additions in parallel */
                Matrix AA[][] = A.split();
                Vector BB[] = B.split();
                Vector LL[] = lhs.split();
                Vector RR[] = rhs.split();

                /*Here we are creating and running a future for each split multiplication task, in each multiplication
                 * we will produce a left side and a right side vector that will eventually need to be added together */
                Future<?> [][] future = (Future<?>[][]) new Future[2][2];
                for(int i = 0; i < 2; i++){
                    future[i][0] = exec.submit(new MultTask(AA[i][0], BB[0], LL[i]));
                    future[i][1] = exec.submit(new MultTask(AA[1][i], BB[1], RR[i]));
                }

                //We are waiting on the results of our futures
                for(int i = 0; i < 2; i++){
                    for(int j = 0; j < 2; j ++){
                        future[i][j].get();
                    }
                }

                /*Here we are adding the left and right vectors together*/
                Future<?> done = exec.submit(new AddTask(lhs, rhs, C));
                done.get();

            }


        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
