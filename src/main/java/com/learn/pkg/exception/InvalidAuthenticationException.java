package com.learn.pkg.exception;

public class InvalidAuthenticationException extends RuntimeException {
  /** */
  private static final long serialVersionUID = -3250866415791608122L;

  public InvalidAuthenticationException(String exception) {
    super(exception);
  }
}
