package com.learn.pkg.service;

import com.learn.pkg.model.CustomerResponse;
import com.learn.pkg.model.kafka.KafkaCustomerDataRequest;

public interface PublisherService {

  public CustomerResponse publishCustomerData(
      KafkaCustomerDataRequest customerPublisherRequest, String transactionId, String activityId);
}
