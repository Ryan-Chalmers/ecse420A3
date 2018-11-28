package ca.mcgill.esce420.a3;

public class Main {

    public static final int size = 2;
    public static Matrix A;
    public static Vector B;

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


        A.printMatrix();
        B.printVector();
        Vector C = sequentialMult(A, B);
        C.printVector();

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
