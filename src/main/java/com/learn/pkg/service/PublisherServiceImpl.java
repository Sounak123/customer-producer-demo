package com.learn.pkg.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learn.pkg.adapter.ProducerAdapter;
import com.learn.pkg.model.Customer;
import com.learn.pkg.model.ModelApiResponse;

@Service
public class PublisherServiceImpl implements PublisherService {
  private static final Logger logger = LoggerFactory.getLogger(PublisherService.class);

  @Autowired private ProducerAdapter producer;

  @Override
  public ModelApiResponse publishCustomerData(Customer customer) {
    ModelApiResponse mSuccess;
    mSuccess = producer.send(customer);

    logger.info("Successfully inserted into kafka, returning response");
    return mSuccess;
  }
}
