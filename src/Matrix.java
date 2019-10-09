public class Matrix extends Thread {

  // static member variables accessible by all instances of Matrix
  static float[][] A;
  static float[][] B;

  static float[][] resultMatrix;

  // instance specific, will be used for multi threading
  int iMin;
  int iMax;
  int jMin;
  int jMax;
  

  // sets static A and B matrices for all instances 
  public static void setMatrices(float[][] _A, float[][] _B) {
    A = _A;
    B = _B;
    resultMatrix = new float[A.length][B[0].length];
  } 

  Matrix(int _iMin, int _iMax, int _jMin, int _jMax) {
    iMin = _iMin;
    iMax = _iMax;
    jMin = _jMin;
    jMax = _jMax;
  }

  // multiple matrices A and B in here, place the result into resultMatrix
  @Override 
  public void run() {

    // CHANGE THIS
    for (int i = iMin; i < iMax; ++i) {
      for (int j = jMin; j < jMax; ++j) {
        resultMatrix[i][j] = 0;
      }
    }

  }

  // to print matrices prettily
  public static void printResult() {
    MatrixLoader.printMatrix(resultMatrix);
  }
  

}