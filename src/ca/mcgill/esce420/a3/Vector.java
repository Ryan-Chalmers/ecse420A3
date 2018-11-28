package ca.mcgill.esce420.a3;

public class Vector {

    int length;
    double[] data;
    int rowDisplace;

    public Vector(int length){
        this.length = length;
        rowDisplace = 0;
        data = new double[length];
    }

    private Vector(double[] vector, int x, int length){
        data = vector;
        rowDisplace = x;
        this.length = length;
    }

    public double get(int row){
        return data[row+rowDisplace];
    }

    public void set(int row, double value){
        data[row + rowDisplace]=value;
    }

    public int getLength() {
        return length;
    }

    public Vector[] split(){
        Vector[] result = new Vector[2];
        int newLength = length/2;
        result[0] = new Vector(data, rowDisplace, newLength);
        result[1] = new Vector(data, rowDisplace+newLength, newLength);
        return result;
    }

    public void printVector(){
        System.out.println("Vector: ");
        for(int i = 0; i <length; i++){
            System.out.println(data[i]);
        }
    }
}
