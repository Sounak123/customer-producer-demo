package com.learn.pkg.exception;

import javax.servlet.http.HttpServletRequest;

import org.apache.kafka.common.errors.TimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.learn.pkg.model.ErrorResponse;
import com.learn.pkg.model.ErrorResponse.StatusEnum;

@ControllerAdvice
public class CustomerControllerAdvice {

  @ExceptionHandler(ServiceException.class)
  public final ResponseEntity<ErrorResponse> handleException(
      ServiceException ex, HttpServletRequest request) {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setStatus(StatusEnum.ERROR);
    errorResponse.setMessage(ex.getMessage());
    errorResponse.setErrorType("Service Unavailable");
    return new ResponseEntity<>(errorResponse, HttpStatus.SERVICE_UNAVAILABLE);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public final ResponseEntity<ErrorResponse> handleException(
      MethodArgumentNotValidException ex, HttpServletRequest request) {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setStatus(StatusEnum.ERROR);
    errorResponse.setMessage(ex.getMessage());
    errorResponse.setErrorType("Bad Request");
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(TimeoutException.class)
  public final ResponseEntity<ErrorResponse> handleException(
      TimeoutException ex, HttpServletRequest request) {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setStatus(StatusEnum.ERROR);
    errorResponse.setMessage(ex.getMessage());
    errorResponse.setErrorType("Service has Timed Out");
    return new ResponseEntity<>(errorResponse, HttpStatus.GATEWAY_TIMEOUT);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public final ResponseEntity<ErrorResponse> handleException(
      AccessDeniedException ex, HttpServletRequest request) {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setStatus(StatusEnum.ERROR);
    errorResponse.setMessage(ex.getMessage());
    errorResponse.setErrorType("Access Is Denied");
    return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
  }
}
