package com.learn.pkg.exception;

public class ServiceException extends RuntimeException {

  /** */
  private static final long serialVersionUID = -8579462339852899011L;

  public ServiceException(String exception) {
    super(exception);
  }
}
