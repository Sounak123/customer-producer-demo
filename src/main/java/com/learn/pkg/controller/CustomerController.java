package com.learn.pkg.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learn.pkg.model.Customer;
import com.learn.pkg.model.ModelApiResponse;
import com.learn.pkg.service.PublisherService;
import com.learn.pkg.util.MaskerUtils;
import com.learn.pkg.util.ObjectMapperUtil;

@RestController
@RequestMapping("v1/customers")
public class CustomerController {
  private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

  @Autowired private MaskerUtils masker;

  @Autowired private PublisherService service;

  @PostMapping("/add_customer_data")
  public ResponseEntity<?> addCustomerData(
      @RequestHeader(value = "Authorization", required = true) String authorization,
      @RequestHeader(value = "activity-id", required = true) String activityId,
      @RequestHeader(value = "application-id", required = true) String applicationId,
      @Valid @RequestBody Customer customer) {
    String customerReqJson = masker.mask(customer);
    logger.info("customer request:" + customerReqJson);
    ModelApiResponse response = service.publishCustomerData(customer);
    logger.info("response:" + ObjectMapperUtil.getJsonFromObj(response));

    return new ResponseEntity<ModelApiResponse>(response, HttpStatus.OK);
  }
}
