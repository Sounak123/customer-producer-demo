package com.learn.pkg.service;

import org.apache.kafka.common.errors.TimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learn.pkg.adapter.ProducerAdapter;
import com.learn.pkg.exception.ServiceException;
import com.learn.pkg.model.Customer;
import com.learn.pkg.model.ModelApiResponse;

@Service
public class PublisherServiceImpl implements PublisherService {

  @Autowired private ProducerAdapter producer;

  @Override
  public ModelApiResponse publishCustomerData(Customer customer) {
    try {
      producer.send(customer);
    } catch (TimeoutException e) {
      throw new ServiceException(e.getMessage());
    }

    return new ModelApiResponse();
  }
}
