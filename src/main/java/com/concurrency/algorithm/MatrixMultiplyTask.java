package com.concurrency.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class MatrixMultiplyTask implements Runnable {
  private final float[][] matrixA;
  private final float[][] matrixB;
  private final float[][] resultMatrix;
  private final int size;
  private final int barrier;

  private int rowMatrixA;
  private int rowMatrixB;
  private int rowMatrixResult;
  private int columnMatrixA;
  private int columnMatrixB;
  private int columnMatrixResult;

  MatrixMultiplyTask( int barrier, float[][] matrixA, float[][] matrixB, float[][] resultMatrix, int size, int rowMatrixA,
                      int rowMatrixB, int rowMatrixResult, int columnMatrixA, int columnMatrixB,
                      int columnMatrixResult )
  {
    this.barrier = barrier;
    this.matrixA = matrixA;
    this.matrixB = matrixB;
    this.resultMatrix = resultMatrix;
    this.size = size;
    this.rowMatrixA = rowMatrixA;
    this.rowMatrixB = rowMatrixB;
    this.rowMatrixResult = rowMatrixResult;
    this.columnMatrixA = columnMatrixA;
    this.columnMatrixB = columnMatrixB;
    this.columnMatrixResult = columnMatrixResult;
  }

  public void run() {

    if ( size <= barrier) {
      multiplicationStep();
    } else {

      int deep = size / 2;

      for( MatrixMultiplyTask matrixMultiplyTask : getSubTasksList( deep ) ) {
       executeSubTask( matrixMultiplyTask );
      }
    }
  }

  private void executeSubTask(MatrixMultiplyTask multiplyTask) {
    final FutureTask futureTask = new FutureTask(multiplyTask, null);
    MultiplyExecution.EXECUTOR.execute( futureTask );
    futureTask.run();
    try {
      futureTask.get();
    }
    catch( InterruptedException | ExecutionException e ) {
      MultiplyExecution.EXECUTOR.shutdownNow();
      //TODO: errors
    }
  }

  private MatrixMultiplyTask createNewTask(int size, int addToRowA, int addToColumnA, int addToRowB,
                                           int addToColumnB, int addToRowResult, int addToColumnResult) {
    return new MatrixMultiplyTask( barrier, matrixA, matrixB, resultMatrix, size,
                                   rowMatrixA + addToRowA,
                                   rowMatrixB + addToRowB,
                                   rowMatrixResult + addToRowResult,
                                   columnMatrixA + addToColumnA,
                                   columnMatrixB + addToColumnB,
                                   columnMatrixResult + addToColumnResult);
  }

  private List<MatrixMultiplyTask> getSubTasksList(int deep) {
    List<MatrixMultiplyTask> subTasksList = new ArrayList<>(  );

    subTasksList.add( createNewTask( deep, 0, 0, 0, 0, 0, 0 ) );// stay
    subTasksList.add( createNewTask( deep, 0, 0, 0, deep, 0, deep ) );// stay and right
    subTasksList.add( createNewTask( deep, deep, 0, 0, 0, deep, 0 ) );// down and stay
    subTasksList.add( createNewTask( deep, 0, deep, deep, 0, 0, 0 ) );// right and down
    subTasksList.add( createNewTask( deep, deep, 0, 0, deep, deep, deep ) );// down and right
    subTasksList.add( createNewTask( deep, 0, deep, deep, deep, 0, deep ) );// right and diagonal
    subTasksList.add( createNewTask( deep, deep, deep, deep, 0, deep, 0 ) );// diagonal and down
    subTasksList.add( createNewTask( deep, deep, deep, deep, deep, deep, deep ) );// diagonal and diagonal

    return subTasksList;
  }


  private void multiplicationStep() {
    for (int i = 0; i < size; ++i) {
      for (int j = 0; j < size; ++j) {
        for (int k = 0; k < size; ++k) {
          resultMatrix[rowMatrixResult+i][columnMatrixResult+j] +=
                  matrixA[rowMatrixA+i][columnMatrixA+k] * matrixB[rowMatrixB+k][columnMatrixB+j];
        }
      }
    }
  }
}