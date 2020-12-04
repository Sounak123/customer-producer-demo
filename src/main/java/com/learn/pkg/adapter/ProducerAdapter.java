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
import com.learn.pkg.model.CustomerResponse;
import com.learn.pkg.model.CustomerResponse.StatusEnum;
import com.learn.pkg.model.kafka.KafkaCustomerDataRequest;
import com.learn.pkg.model.kafka.PublisherRequest;
import com.learn.pkg.util.ObjectMapperUtil;

@Component
public class ProducerAdapter {
  private static final Logger logger = LoggerFactory.getLogger(ProducerAdapter.class);

  @Autowired private KafkaTemplate<String, PublisherRequest> kafkaTemplate;

  @Autowired private CustomerDataMasker customerPublisherDataMasker;

  @Value("${cloudkarafka.topic}")
  private String topic;

  public CustomerResponse send(PublisherRequest message) {

    KafkaCustomerDataRequest data = customerPublisherDataMasker.convert(message.getCustomerData());

    ListenableFuture<SendResult<String, PublisherRequest>> future =
        this.kafkaTemplate.send(topic, message);

    future.addCallback(
        new ListenableFutureCallback<SendResult<String, PublisherRequest>>() {

          @Override
          public void onSuccess(SendResult<String, PublisherRequest> result) {
            logger.info("Sent data [{}] to topic:{}", ObjectMapperUtil.getJsonFromObj(data), topic);
          }

          @Override
          public void onFailure(Throwable ex) {
            logger.error("Failed to send data");
          }
        });
    try {
      future.get();
      return successResponse();
    } catch (InterruptedException | ExecutionException e) {
      throw new ServiceException(e.getMessage());
    }
  }

  private CustomerResponse successResponse() {
    CustomerResponse customerResponse = new CustomerResponse();
    customerResponse.setMessage("Data Successfully Sent.");
    customerResponse.setStatus(StatusEnum.SUCCESS);
    return customerResponse;
  }
}
