package com.learn.pkg.exception;

import javax.servlet.http.HttpServletRequest;

import org.apache.kafka.common.errors.TimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.learn.pkg.model.ErrorResponse;
import com.learn.pkg.model.ErrorResponse.StatusEnum;

@ControllerAdvice(basePackages = "com.learn.pkg")
public class CustomerControllerAdvice {

  @ExceptionHandler(ServiceException.class)
  public final ResponseEntity<ErrorResponse> handleException(
      ServiceException ex, HttpServletRequest request) {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setStatus(StatusEnum.ERROR);
    errorResponse.setMessage(ex.getMessage());
    return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(TimeoutException.class)
  public final ResponseEntity<ErrorResponse> handleException(
      TimeoutException ex, HttpServletRequest request) {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setStatus(StatusEnum.ERROR);
    errorResponse.setMessage(ex.getMessage());
    errorResponse.setErrorType("Service has Timed Out");
    return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.GATEWAY_TIMEOUT);
  }
}
