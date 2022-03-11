import java.util.*;
public class Main {

public static void main(String[] args){
	int [][] firstMatrix= randomMatrix(5);
	printMatrix(firstMatrix);
}

static int[][] randomMatrix(int n) {
	Random r=new Random();
	int[][] a=new int[n][n];
	for(int i=0;i<n;i++) {
    	for(int j=0;j<n;j++) {
        	a[i][j]=r.nextInt(20);
      	}
	}
	printMatrix(a);
	System.out.print("\n");
	return a;
}

public static void printMatrix(int mat[][]) {
        for (int[] row : mat)
            System.out.println(Arrays.toString(row));
}
}

