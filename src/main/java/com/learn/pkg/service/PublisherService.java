package com.learn.pkg.service;

import com.learn.pkg.model.Customer;
import com.learn.pkg.model.ModelApiResponse;

public interface PublisherService {

  public ModelApiResponse publishCustomerData(Customer customer);
}
