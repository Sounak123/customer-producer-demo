package com.learn.pkg.service;

import com.learn.pkg.model.CustomerResponse;
import com.learn.pkg.model.kafka.PublisherRequest;

public interface PublisherService {

  public CustomerResponse publishCustomerData(PublisherRequest customerPublisherRequest);
}
