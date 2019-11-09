package com.multiply.exceptions;

public class WrongMatrixSizeException extends Exception {

  private static final String errorMessage = "Matrix sizes are not compatible. Input square matrix with same size.";

  public WrongMatrixSizeException() {
    super( errorMessage );
  }

}
