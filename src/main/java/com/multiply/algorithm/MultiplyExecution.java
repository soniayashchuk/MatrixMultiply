package com.multiply.algorithm;

import com.multiply.exceptions.WrongMatrixSizeException;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MultiplyExecution
{
  static ExecutorService EXECUTOR = Executors.newFixedThreadPool( Runtime.getRuntime().availableProcessors() );

  private int barrier;
  private float[][] matrixA;
  private float[][] matrixB;
  private float[][] resultMatrix;
  private int matrixSize;

  public MultiplyExecution( int size) {
    this.matrixSize = size;
    this.matrixA = generateRandomMatrix( size);
    this.matrixB = generateRandomMatrix( size);
    this.resultMatrix = new float[matrixSize][matrixSize];
    this.barrier = matrixSize;
  }

  public MultiplyExecution( float[][] matrixA, float[][] matrixB) {
    this.matrixA = matrixA;
    this.matrixB = matrixB;
    this.matrixSize = matrixA.length;
    this.resultMatrix = new float[matrixSize][matrixSize];
    this.barrier = matrixSize ;
  }

  public void execute() throws WrongMatrixSizeException
  {
    checkMatrixSize();

    MatrixMultiplyTask mainTask =  new MatrixMultiplyTask(barrier, matrixA, matrixB, resultMatrix, matrixSize,
                                                           0, 0, 0, 0, 0,0 );
    Future future = EXECUTOR.submit( mainTask);

    try {
      future.get();
    } catch (Exception e) {
      EXECUTOR.shutdownNow();
      //TODO: add logger
    }
  }

  public float[][] getResultMatrix()
  {
    return resultMatrix;
  }

  public void printResults() {
    System.out.println( "\nFirst matrix: " );
    printMatrix( matrixA );
    System.out.println( "\nSecond matrix: " );
    printMatrix( matrixB );
    System.out.println( "\nResult matrix: " );
    printMatrix(resultMatrix);
  }

  private float[][] generateRandomMatrix( int size ) {
    Random random = new Random(  );

    float[][] randomMatrix = new float[size][size];
    for ( int i = 0; i < size; i++ )
    {
      for ( int j = 0; j < size; j++ )
      {
        randomMatrix[i][j] = random.nextFloat();
      }
    }
    return randomMatrix;
  }

  private void printMatrix(float[][] toPrint) {
    for ( float[] row : toPrint ) {
      for ( float value : row ) {
        System.out.print( value + " ");
      }
      System.out.println();
    }
  }

  private void checkMatrixSize() throws WrongMatrixSizeException
  {
    if ( matrixA.length != matrixB.length || matrixA[0].length != matrixB[0].length ) {
      throw new WrongMatrixSizeException();
    }
  }

}
