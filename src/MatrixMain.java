import java.util.*;  

public class MatrixMain {
  public static void main(String[] args) {

    // initialize a couple of 20x20 matrices
    // float[][] resB = MatrixLoader.getIdentityMatrixMxN(20, 20);
    // float[][] resA = MatrixLoader.getStructuredMatrixMxN(20, 20, 0, 0, 1);

    float[][] resA = MatrixLoader.getRandomMatrixMxN(20, 20, 0, 0, 1);
    float[][] resB = MatrixLoader.getRandomMatrixMxN(20, 20, 0, 0, 1);
    System.out.println("Matrix A");
    MatrixLoader.printMatrix(resA);
    System.out.println("Matrix B");
    MatrixLoader.printMatrix(resB);

    // set the matrices to be used for all Matrix instances / threads 
    Matrix.setMatrices(resA, resB);

    // create single thread for now 
    int nThreads = 5;
    List<Thread> threads = new ArrayList<Thread>();

    if (nThreads > resA.length) {
      System.out.println("Error: more threads than rows in matrix. I have set the threads equal to the number of rows...");
      nThreads = resA.length;
      threads = new ArrayList<Thread>(nThreads);
    }
    int step = resA.length / nThreads;
    int residual = resA.length % nThreads;
    int offset = 0;
    System.out.printf("rows per threads (step size): %d\n", step);
    System.out.printf("residual rows (left over): %d\n", residual);
    // instance thread(s) that will multiply A and B
    for (int i = 0; i < nThreads; ++i) {
      // System.out.printf("threads size: %d\n", threads.size());
      // threads[i] = new Matrix(0, resA.length, 0, resB[0].length);
      threads.add(new Matrix(offset, offset + step, 0, resB[0].length));
      offset += step;
      // multiplies the matrices
      threads.get(i).start();
    }
    if (residual > 0) {
      // System.out.printf("offset curr in residual: %d\n", offset);
      threads.add(new Matrix(offset, offset + residual, 0, resB[0].length));
      threads.get(threads.size()-1).start();
    }


    // join threads 
    try {
      for (int i = 0; i < threads.size(); ++i) {
        threads.get(i).join();
      }
    } catch (Exception e) {
      System.out.println("Exception has been caught" + e);
    }

    // prints result
    System.out.println("result");
    Matrix.printResult();

  }

}
