package ca.mcgill.esce420.a3;

public class Main {

    public static final int size = 2;
    public static Matrix A;
    public static Matrix B;

    public static void main(String args[]){

        A = new Matrix(size);
        B = new Matrix(size);

        //populate matrices
        for(int i = 0; i <size; i++){
            for(int j = 0; j < size; j++){
                A.data[i][j] = 1;
                B.data[i][j] = 1;
            }
        }

        A.printMatrix();
        B.printMatrix();
        Matrix C = sequentialMult(A, B);
        C.printMatrix();

    }

    public static Matrix sequentialMult(Matrix A, Matrix B){
        Matrix C = new Matrix(size);
        for(int r = 0; r < size; r++){
            for(int c = 0; c < size; c++){
                int sum = 0;
                for(int k = 0; k < size; k++){
                    sum += A.data[r][k]*B.data[k][c];
                }
                C.data[r][c]=sum;
            }
        }
        return C;
    }



}
