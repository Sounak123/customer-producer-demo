package com.learn.pkg.service;

import com.learn.pkg.model.Customer;
import com.learn.pkg.model.CustomerResponse;

public interface PublisherService {

  public CustomerResponse publishCustomerData(Customer customer, String transactionId);
}
