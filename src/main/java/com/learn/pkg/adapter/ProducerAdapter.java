package com.learn.pkg.adapter;

import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.learn.pkg.converter.CustomerDataMasker;
import com.learn.pkg.exception.ServiceException;
import com.learn.pkg.model.Customer;
import com.learn.pkg.model.CustomerResponse;
import com.learn.pkg.model.CustomerResponse.StatusEnum;
import com.learn.pkg.model.kafka.KafkaCustomerDataRequest;
import com.learn.pkg.util.ObjectMapperUtil;

@Component
public class ProducerAdapter {
  private static final Logger logger = LoggerFactory.getLogger(ProducerAdapter.class);

  @Autowired private KafkaTemplate<String, KafkaCustomerDataRequest> kafkaTemplate;

  @Autowired private CustomerDataMasker masker;

  @Value("${cloudkarafka.topic}")
  private String topic;

  public CustomerResponse send(Customer message, String transactionId) {

    KafkaCustomerDataRequest data = masker.convert(message);

    ListenableFuture<SendResult<String, KafkaCustomerDataRequest>> future =
        this.kafkaTemplate.send(topic, transactionId, data);

    future.addCallback(
        new ListenableFutureCallback<SendResult<String, KafkaCustomerDataRequest>>() {

          @Override
          public void onSuccess(SendResult<String, KafkaCustomerDataRequest> result) {
            logger.info("Sent data [" + ObjectMapperUtil.getJsonFromObj(data) + "] to " + topic);
          }

          @Override
          public void onFailure(Throwable ex) {
            logger.error("Failed to send data");
          }
        });
    try {
      future.get();
      CustomerResponse mSuccess = new CustomerResponse();
      mSuccess.setMessage("Data Successfully Sent.");
      mSuccess.setStatus(StatusEnum.SUCCESS);
      return mSuccess;
    } catch (InterruptedException | ExecutionException e) {
      throw new ServiceException(e.getMessage());
    }
  }
}
