package com.multiply;

import com.multiply.algorithm.MultiplyExecution;
import com.multiply.exceptions.WrongMatrixSizeException;

import java.util.Scanner;

public class Run
{
  public static void main( String[] args ) throws WrongMatrixSizeException
  {
    Scanner scanner = new Scanner( System.in );
    System.out.print( "\nEnter size of matrix to multiply: ");

    int size = scanner.nextInt();

    MultiplyExecution multiplyExecution = new MultiplyExecution( size );
    multiplyExecution.execute();

    multiplyExecution.printResults();
  }
}
