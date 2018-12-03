package ca.mcgill.esce420.a3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    public static final int size = 2000;
    public static Matrix A;
    public static Vector B;
    static ExecutorService exec = Executors.newCachedThreadPool();

    public static void main(String args[]){

        A = new Matrix(size);
        B = new Vector(size);

        //populate matrix/vector
        for(int i = 0; i <size; i++){
            for(int j = 0; j < size; j++){
                A.data[i][j] = 1;
            }
            B.data[i] = 1;
        }

        //Sequentially multipy matrices
        long start = System.currentTimeMillis();
        Vector C = sequentialMult(A, B);
        long end = System.currentTimeMillis();
        long duration = end - start;
        System.out.println("Sequential: " + duration);


        try {
            start = System.currentTimeMillis();
            multiply(A, B);
            end = System.currentTimeMillis();
            duration = end - start;
            System.out.println("Parallel: " + duration);
        } catch (Exception e){
            e.printStackTrace();
        }

    }


    public static Vector multiply(Matrix A, Vector B) throws Exception {
        int length = B.getLength();
        Vector C = new Vector(length);
        Future<?> future = exec.submit(new MultTask(A,B,C));
        future.get();
        return C;
    }

    public static Vector sequentialMult(Matrix A, Vector B){
        Vector C = new Vector(size);
        for(int r = 0; r < size; r++){
            int sum = 0;
            for(int k = 0; k < size; k++){
                sum += A.data[r][k]*B.data[k];
            }
            C.data[r] = sum;
        }
        return C;
    }
    

}
