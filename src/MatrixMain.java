public class MatrixMain {
  public static void main(String[] args) {

    // initialize a couple of 20x20 matrices
    float[][] resA = MatrixLoader.getRandomMatrixMxN(20, 20,   0,  0,  1);
    float[][] resB = MatrixLoader.getRandomMatrixMxN(20, 20,   0,  0,  1);
    System.out.println("Matrix A");
    MatrixLoader.printMatrix(resA);
    System.out.println("Matrix B");
    MatrixLoader.printMatrix(resB);

    // set the matrices to be used for all Matrix instances / threads 
    Matrix.setMatrices(resA, resB);

    // create single thread for now 
    int nThreads = 1;
    Thread[] threads = new Thread[nThreads];

    for (int i = 0; i < threads.length; ++i) {
      // instance thread that will multiply A and B
      threads[i] = new Matrix(0, resA.length, 0, resB[0].length);
      // multiplies the matrices
      threads[i].start();
    }


    // join threads 
    try {
      for (int i = 0; i < threads.length; ++i) {
        threads[i].join();
      }
    } catch (Exception e) {
      System.out.println("Exception has been caught" + e);
    }

    // prints result
    System.out.println("result");
    Matrix.printResult();

  }

}