package com.multiply;

import com.multiply.algorithm.MultiplyExecution;
import com.multiply.exceptions.WrongMatrixSizeException;
import org.junit.Assert;
import org.junit.Test;

public class SimpleTest {

  @Test
  public void multiplyMatrixWithDoubleSizeShouldReturnCorrectResult() throws WrongMatrixSizeException
  {
    float[][] firstMatrix = { { 2, 5 }, { 3, 7 }};
    float[][] secondMatrix = { { 4, 8 }, { 1, 6 }};

    MultiplyExecution multiplyExecution = new MultiplyExecution( firstMatrix, secondMatrix );
    multiplyExecution.execute();
    float[][] resultMatrix = multiplyExecution.getResultMatrix();

    float[][] expectedMatrix = { { 13, 46 }, { 19, 66 }};

    for ( int i = 0; i < firstMatrix.length; i++) {
      for ( int j = 0; j < secondMatrix.length; j++ ) {
        Assert.assertEquals( expectedMatrix[i][j], resultMatrix[i][j], 0.0001 );
      }
    }
  }

  @Test( expected = WrongMatrixSizeException.class)
  public void multiplyMatrixWithWrongSizeShouldThrowException() throws WrongMatrixSizeException
  {
    float[][] firstMatrix = { { 2, 5, 3 }, { 3, 7 }};
    float[][] secondMatrix = { { 4, 8 }, { 1, 6 }};

    MultiplyExecution multiplyExecution = new MultiplyExecution( firstMatrix, secondMatrix );
    multiplyExecution.execute();
  }

}
