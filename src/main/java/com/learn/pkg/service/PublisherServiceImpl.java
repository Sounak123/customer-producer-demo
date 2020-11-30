package com.learn.pkg.service;

import org.apache.kafka.common.errors.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learn.pkg.adapter.ProducerAdapter;
import com.learn.pkg.exception.ServiceException;
import com.learn.pkg.model.Customer;
import com.learn.pkg.model.ModelApiResponse;

@Service
public class PublisherServiceImpl implements PublisherService {
  private static final Logger logger = LoggerFactory.getLogger(PublisherService.class);

  @Autowired private ProducerAdapter producer;

  @Override
  public ModelApiResponse publishCustomerData(Customer customer) {
    ModelApiResponse mSuccess;
    try {
      mSuccess = producer.send(customer);
    } catch (TimeoutException e) {
      throw new ServiceException(e.getMessage());
    } catch (Exception e) {
      throw new ServiceException(e.getMessage());
    }

    logger.info("Successfully inserted into kafka, returning response");
    return mSuccess;
  }
}
