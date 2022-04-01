import java.util.Arrays;
import java.util.Random;

public class Main {

    // --- Test Matrices ---

    private static int[][] test1 = {
        {2, 0, -1, 6},
        {3, 7, 8, 0},
        {-5, 1, 6, -2},
        {8, 0, 2, 7}
    };

    private static int[][] test2 = {
        {0, 1, 6, 3},
        {-2, 8, 7, 1},
        {2, 0, -1, 0},
        {9, 1, 6, -2}
    };

    /* Solution for test1 * test2 = 
        
        |   52  8   49  -6  |
        |   2   59  59  16  |
        |   -8  1   -41 -10 |
        |   67  15  88  10  |

    */

    // --- End of Test Matrices ---

    // Generate Random Matrices of size n x n
    private static int[][] randomMatrix(int n) {
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

    // Print matrix values to output
    private static void printMatrix(int[][] matrix) {
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }

            System.out.println();
        }
        System.out.println();
    }

    // Multiply 2 n x n matrices using the classic "brute force" method
    private static int[][] classicMatrixMult(int[][] a, int[][] b) {
        //Starting the runtime count in nanoseconds
        long start = System.nanoTime();
        // Length of rows and columns of matrices (Each matrix is assumed to be of size n x n)
        int n = a.length;

        // Result matrix from calculation
        int[][] result = new int[n][n];

        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                // Current entry in matrix being calculated
                int curEntry = 0;

                // Multiply the correct entries in the current row of 'a' and column of 'b'
                for(int k = 0; k < n; k++) {
                    curEntry += (a[i][k] * b[k][j]);
                }

                result[i][j] = curEntry;
            }
        }
        //Calculating the runtime in nanoseconds
        long end = System.nanoTime();
        long elapsedTime = end - start;
        System.out.println("Elapsed Time in nanoseconds: " + elapsedTime);
        return result;
    }

    // Multiply 2 n x n matrices using the naive divide and conquer method
    private static int[][] naiveDivConquerMatrixMult(int[][] a, int[][] b) {
        // Length of rows and columns of matrices (Each matrix is assumed to be of size n x n)
        int n = a.length;

        // Result matrix from calculation
        int[][] result = new int[n][n];

        // Base case: multiplying 2 1 x 1 matrices
        if(n == 1) {
            result[0][0] = a[0][0] * b[0][0];
            return result;
        }

        // Partition matrices into four quadrants

        int[][] a1 = matrixPartition(a, 0, n / 2, 0, n / 2);    // Represents A1,1 from slides
        int[][] a2 = matrixPartition(a, n / 2, n, 0, n / 2);    // Represents A1,2 from slides
        int[][] a3 = matrixPartition(a, 0, n / 2, n / 2, n);    // Represents A2,1 from slides
        int[][] a4 = matrixPartition(a, n / 2, n, n / 2, n);    // Represents A2,2 from slides

        int[][] b1 = matrixPartition(b, 0, n / 2, 0, n / 2);    // Represents B1,1 from slides
        int[][] b2 = matrixPartition(b, n / 2, n, 0, n / 2);    // Represents B1,2 from slides
        int[][] b3 = matrixPartition(b, 0, n / 2, n / 2, n);    // Represents B2,1 from slides
        int[][] b4 = matrixPartition(b, n / 2, n, n / 2, n);    // Represents B2,2 from slides

        // Calculate each quadrant of result matrix
        int[][] result1 = matrixAdd(naiveDivConquerMatrixMult(a1, b1), naiveDivConquerMatrixMult(a2, b3));
        int[][] result2 = matrixAdd(naiveDivConquerMatrixMult(a1, b2), naiveDivConquerMatrixMult(a2, b4));
        int[][] result3 = matrixAdd(naiveDivConquerMatrixMult(a3, b1), naiveDivConquerMatrixMult(a4, b3));
        int[][] result4 = matrixAdd(naiveDivConquerMatrixMult(a3, b2), naiveDivConquerMatrixMult(a4, b4));

        // Combine result quadrants into a single result matrix
        result = matrixCombine(result1, result2, result3, result4);

        return result;
    }

    // Multiply 2 n x n matrices using the strassen algorithm
    private static int[][] strassenMatrixMult(int[][] a, int[][] b) {
        // Length of rows and columns of matrices (Each matrix is assumed to be of size n x n)
        int n = a.length;

        // Result matrix from calculation
        int[][] result = new int[n][n];

        // Base case: multiplying 2 1 x 1 matrices
        if(n == 1) {
            result[0][0] = a[0][0] * b[0][0];
            return result;
        }

        // Partition matrices into four quadrants

        int[][] a1 = matrixPartition(a, 0, n / 2, 0, n / 2);    // Represents A1,1 from slides
        int[][] a2 = matrixPartition(a, n / 2, n, 0, n / 2);    // Represents A1,2 from slides
        int[][] a3 = matrixPartition(a, 0, n / 2, n / 2, n);    // Represents A2,1 from slides
        int[][] a4 = matrixPartition(a, n / 2, n, n / 2, n);    // Represents A2,2 from slides

        int[][] b1 = matrixPartition(b, 0, n / 2, 0, n / 2);    // Represents B1,1 from slides
        int[][] b2 = matrixPartition(b, n / 2, n, 0, n / 2);    // Represents B1,2 from slides
        int[][] b3 = matrixPartition(b, 0, n / 2, n / 2, n);    // Represents B2,1 from slides
        int[][] b4 = matrixPartition(b, n / 2, n, n / 2, n);    // Represents B2,2 from slides

        // Recursively calculate products
        int[][] p1 = strassenMatrixMult(a1, matrixSub(b2, b4));
        int[][] p2 = strassenMatrixMult(matrixAdd(a1, a2), b4);
        int[][] p3 = strassenMatrixMult(matrixAdd(a3, a4), b1);
        int[][] p4 = strassenMatrixMult(a4, matrixSub(b3, b1));
        int[][] p5 = strassenMatrixMult(matrixAdd(a1, a4), matrixAdd(b1, b4));
        int[][] p6 = strassenMatrixMult(matrixSub(a2, a4), matrixAdd(b3, b4));
        int[][] p7 = strassenMatrixMult(matrixSub(a1, a3), matrixAdd(b1, b2));

        // Calculate each quadrant of result matrix
        int[][] result1 = matrixAdd(matrixAdd(matrixSub(p4, p2), p5), p6);
        int[][] result2 = matrixAdd(p1, p2);
        int[][] result3 = matrixAdd(p3, p4);
        int[][] result4 = matrixSub(matrixAdd(matrixSub(p1, p3), p5), p7);

        // Combine result quadrants into a single result matrix
        result = matrixCombine(result1, result2, result3, result4);
        
        return result;
    }

    // Partition matrix into a smaller submatrix (assumed to be n x n. start ints inclusive, end ints exlusive)
    private static int[][] matrixPartition(int[][] matrix, int startX, int endX, int startY, int endY) {
        // Size of new matrix
        int n = endX - startX;

        // Result submatrix
        int[][] result = new int[n][n];
        
        // Copy necessary elements to new matrix
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                result[i][j] = matrix[startY + i][startX + j];
            }
        }

        return result;
    }

    // Add 2 n x n matrices
    private static int[][] matrixAdd(int[][] a, int[][] b) {
        // Size of new matrix
        int n = a.length;

        // Resulting 
        int[][] result = new int[n][n];

        // Add corresponding elements
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                result[i][j] = a[i][j] + b[i][j];
            }
        }

        return result;
    }

    // Subtract 2 n x n matrices
    private static int[][] matrixSub(int[][] a, int[][] b) {
        // Size of new matrix
        int n = a.length;

        // Resulting 
        int[][] result = new int[n][n];

        // Subtract corresponding elements
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                result[i][j] = a[i][j] - b[i][j];
            }
        }

        return result;
    }

    // Combine matrix quadrants into a single matrix (input matrices assumed to all be n x n)
    private static int[][] matrixCombine(int[][] matrix1, int[][] matrix2, int[][] matrix3, int[][] matrix4) {
        // Size of new matrix will be 2n x 2n
        int n = matrix1.length;

        // Combined matrix
        int[][] result = new int[2 * n][2 * n];

        // Copy elements from each input matrix to correct quadrants
        for(int i = 0; i < 2 * n; i++) {
            for(int j = 0; j < 2 * n; j++) {
                // Determine which quadrant the loop is in, then assign element to result accordingly
                if(i < n && j < n) {
                    result[i][j] = matrix1[i][j];
                }
                else if(i < n && j >= n) {
                    result[i][j] = matrix2[i][j - n];
                }
                else if(i >= n && j < n) {
                    result[i][j] = matrix3[i - n][j];
                }
                else {
                    result[i][j] = matrix4[i - n][j - n];
                }
            }
        }

        return result;
    }

    public static void main(String[] args) {
        //Display Test Matrices
        System.out.println("Test Matrix One:\n");
        printMatrix(test1);
        System.out.println("Test Matrix Two:\n");
        printMatrix(test2);


        // Test classic matrix multiplication
        System.out.println("Classic Matrix Multiplication:\n");
        printMatrix(classicMatrixMult(test1, test2));

        // Test naive divide and conquer matrix multiplication
        System.out.println("Naive Divide and Conquer Matrix Multiplication:\n");
            long start = System.nanoTime(); //Starting the runtime count in nanoseconds
            int [][] result = naiveDivConquerMatrixMult(test1, test2);
            long end = System.nanoTime(); //Calculating the runtime in nanoseconds
            long elapsedTime = end - start;
            System.out.println("Elapsed Time in nanoseconds: " + elapsedTime);
        printMatrix(result);

        // Test strassen matrix multiplication
        System.out.println("Strassen Matrix Multiplication:\n");
            start = System.nanoTime();
            result = strassenMatrixMult(test1, test2);
            end = System.nanoTime(); //Calculating the runtime in nanoseconds
            elapsedTime = end - start;
            System.out.println("Elapsed Time in nanoseconds: " + elapsedTime);
        printMatrix(result);

        // Multiplying random matrices
        testRandom(16);
        
    }

    // Multiplying random matrices of size n x n
    private static void testRandom(int n) {

        System.out.println("Classical Multiplication with Random Matrices of size " + n + " x "+ n +":\n");
        printMatrix(classicMatrixMult(randomMatrix(n), randomMatrix(n))); 

        System.out.println("Naive Divide and Conquer Multiplication with Random Matrices of size " + n + " x "+ n +":\n");
        int [][] randomMatrix1 = randomMatrix(n);
        int [][] randomMatrix2 = randomMatrix(n);
            long start = System.nanoTime();
            int [][] result = naiveDivConquerMatrixMult(randomMatrix1, randomMatrix2);
            long end = System.nanoTime(); 
            long elapsedTime = end - start; //Calculating the runtime in nanoseconds
            System.out.println("Elapsed Time in nanoseconds: " + elapsedTime);
        printMatrix(result);

        System.out.println("Strassen Multiplication with Random Matrices of size " + n + " x "+ n +":\n");
        randomMatrix1 = randomMatrix(n);
        randomMatrix2 = randomMatrix(n);
            start = System.nanoTime();
            result = strassenMatrixMult(randomMatrix1, randomMatrix2);
            end = System.nanoTime(); 
            elapsedTime = end - start; //Calculating the runtime in nanoseconds
            System.out.println("Elapsed Time in nanoseconds: " + elapsedTime);
        printMatrix(result);
    }
}