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

import com.learn.pkg.converter.ObjectMasker;
import com.learn.pkg.exception.ServiceException;
import com.learn.pkg.model.Customer;
import com.learn.pkg.model.ModelApiResponse;
import com.learn.pkg.model.ModelApiResponse.StatusEnum;
import com.learn.pkg.util.ObjectMapperUtil;

@Component
public class ProducerAdapter {
  private static final Logger logger = LoggerFactory.getLogger(ProducerAdapter.class);

  @Autowired private KafkaTemplate<String, Customer> kafkaTemplate;

  @Autowired private ObjectMasker masker;

  @Value("${cloudkarafka.topic}")
  private String topic;

  public ModelApiResponse send(Customer message) {

    ListenableFuture<SendResult<String, Customer>> future = this.kafkaTemplate.send(topic, message);

    future.addCallback(
        new ListenableFutureCallback<SendResult<String, Customer>>() {

          @Override
          public void onSuccess(SendResult<String, Customer> result) {
            logger.info(
                "Sent data ["
                    + ObjectMapperUtil.getJsonFromObj(masker.convert(message))
                    + "] to "
                    + topic);
          }

          @Override
          public void onFailure(Throwable ex) {
            logger.error("Failed to send data");
          }
        });
    try {
      future.get();
      ModelApiResponse mSuccess = new ModelApiResponse();
      mSuccess.setMessage("Data Successfully Sent.");
      mSuccess.setStatus(StatusEnum.SUCCESS);
      return mSuccess;
    } catch (InterruptedException | ExecutionException e) {
      throw new ServiceException(e.getMessage());
    }
  }
}
