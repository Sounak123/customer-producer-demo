package com.learn.pkg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learn.pkg.adapter.ProducerAdapter;
import com.learn.pkg.model.Customer;
import com.learn.pkg.model.CustomerResponse;

@Service
public class PublisherServiceImpl implements PublisherService {

  @Autowired private ProducerAdapter producer;

  @Override
  public CustomerResponse publishCustomerData(Customer customer, String transactionId) {
    return producer.send(customer, transactionId);
  }
}
