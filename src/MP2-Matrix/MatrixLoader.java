import java.io.*;
import java.util.Random;

public class MatrixLoader {

  public static float[][] readMatrixFromFile(String filename) {
    System.out.println("file: " + filename);
    File file = new File(filename);
    float res[][] = new float[0][0];
    try {
      BufferedReader br = new BufferedReader(new FileReader(file));
      String str;

      int rows = Integer.parseInt(br.readLine().trim());
      int cols = Integer.parseInt(br.readLine().trim());

      res = new float[rows][cols];

      System.out.println("rows: " + rows);
      System.out.println("cols: " + cols);

      for (int i = 0; i < rows; ++i) {
        String[] strRow = br.readLine().split(" ");
        for (int j = 0; j < strRow.length; ++j) {
          // System.out.println(j + ": " + strRow[j]);
          res[i][j] = Float.parseFloat(strRow[j]);
          // System.out.print(res[i][j] + " ");
        }
        // System.out.println();

      }

      br.close();

    } catch (Exception e) {
      System.out.println("error: " + e);
    }

    return res;
  }

  public static float[][] getRandomMatrixMxN(int m, int n,  float floor, float min, float max) {

    Random rand = new Random();

    float[][] res = new float[m][n];
    for (int i = 0; i < res.length; ++i) {
      for (int j = 0; j < res[i].length; ++j) {
        res[i][j] = rand.nextFloat() * (max-min) + floor;
      }
    }
    
    return res;

  }

  public static float[][] getStructuredMatrixMxN(int m, int n,  float floor, float min, float max) {

    // Random rand = new Random();

    float[][] res = new float[m][n];
    for (int i = 0; i < res.length; ++i) {
      for (int j = 0; j < res[i].length; ++j) {
        res[i][j] = (i + j) * (max - min) + floor;
      }
    }
    
    return res;

  }

  public static float[][] getIdentityMatrixMxN(int m, int n) {

    // Random rand = new Random();

    float[][] res = new float[m][n];
    for (int i = 0; i < res.length; ++i) {
      for (int j = 0; j < res[i].length; ++j) {
        if (i == j ) {
          res[i][j] = 1;
        }
        else {
          res[i][j] = 0;
        }
      }
    }
    
    return res;

  }
  

  public static void printMatrix(float[][] mat) {
    for (int i = 0; i < mat.length; ++i) {
      for (int j = 0; j < mat[i].length; ++j) {
        System.out.printf("%8.2f ", mat[i][j]);
      }
      System.out.println();
    }
  }
}