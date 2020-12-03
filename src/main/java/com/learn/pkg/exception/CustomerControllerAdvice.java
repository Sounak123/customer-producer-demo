package com.learn.pkg.exception;

import javax.servlet.http.HttpServletRequest;

import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.learn.pkg.model.ErrorResponse;
import com.learn.pkg.model.ErrorResponse.StatusEnum;

@ControllerAdvice
public class CustomerControllerAdvice {
  Logger logger = LoggerFactory.getLogger(CustomerControllerAdvice.class);

  @ExceptionHandler(ServiceException.class)
  public final ResponseEntity<ErrorResponse> handleException(
      ServiceException ex, HttpServletRequest request) {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setStatus(StatusEnum.ERROR);
    errorResponse.setMessage(ex.getMessage());
    errorResponse.setErrorType("Service Unavailable");
    logger.error("Service unavailable: {}", ex.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.SERVICE_UNAVAILABLE);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public final ResponseEntity<ErrorResponse> handleException(
      MethodArgumentNotValidException ex, HttpServletRequest request) {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setStatus(StatusEnum.ERROR);
    errorResponse.setMessage(ex.getMessage());
    errorResponse.setErrorType("Bad Request");
    logger.error("Bad Request: {}", ex.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(TimeoutException.class)
  public final ResponseEntity<ErrorResponse> handleException(
      TimeoutException ex, HttpServletRequest request) {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setStatus(StatusEnum.ERROR);
    errorResponse.setMessage(ex.getMessage());
    errorResponse.setErrorType("Service has Timed Out");
    logger.error("Service has Timed Out: {}", ex.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.GATEWAY_TIMEOUT);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public final ResponseEntity<ErrorResponse> handleException(
      AccessDeniedException ex, HttpServletRequest request) {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setStatus(StatusEnum.ERROR);
    errorResponse.setMessage(ex.getMessage());
    errorResponse.setErrorType("Access Is Denied");
    logger.error("Access Is Denied: {}", ex.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  public final ResponseEntity<ErrorResponse> handleException(
      NoHandlerFoundException ex, HttpServletRequest request) {

    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setStatus(StatusEnum.ERROR);
    errorResponse.setMessage(ex.getMessage());
    errorResponse.setErrorType("Resource not found!");
    logger.error("Resource not found!: {}", ex.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(KafkaException.class)
  public final ResponseEntity<ErrorResponse> handleException(
      KafkaException ex, HttpServletRequest request) {

    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setStatus(StatusEnum.ERROR);
    errorResponse.setMessage(ex.getMessage());
    errorResponse.setErrorType("Unable to process request");
    logger.error("Unable to process request: {}", ex.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
