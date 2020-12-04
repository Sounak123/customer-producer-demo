package com.learn.pkg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learn.pkg.adapter.ProducerAdapter;
import com.learn.pkg.model.CustomerResponse;
import com.learn.pkg.model.kafka.KafkaCustomerDataRequest;
import com.learn.pkg.model.kafka.PublisherRequest;

@Service
public class PublisherServiceImpl implements PublisherService {

  @Autowired private ProducerAdapter producer;

  @Override
  public CustomerResponse publishCustomerData(
      KafkaCustomerDataRequest customerPublisherRequest, String transactionId, String activityId) {

    PublisherRequest publisherRequest =
        new PublisherRequest(customerPublisherRequest, transactionId, activityId);
    return producer.send(publisherRequest);
  }
}
