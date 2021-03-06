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

import com.learn.pkg.converter.CustomerDataConverter;
import com.learn.pkg.converter.KafkaCustomerDataRequestConverter;
import com.learn.pkg.model.Customer;
import com.learn.pkg.model.CustomerResponse;
import com.learn.pkg.model.kafka.KafkaCustomerDataRequest;
import com.learn.pkg.model.kafka.PublisherRequest;
import com.learn.pkg.service.PublisherService;
import com.learn.pkg.util.ObjectMapperUtil;

@RestController
@RequestMapping("v1/customers")
public class CustomerController {
  private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

  @Autowired private KafkaCustomerDataRequestConverter customerPublisherDataMasker;

  @Autowired private CustomerDataConverter customePublisherDataConverter;

  @Autowired private PublisherService service;

  @PostMapping("/add-customer-data")
  public ResponseEntity<CustomerResponse> addCustomerData(
      @RequestHeader(value = "Authorization", required = true) String authorization,
      @RequestHeader(value = "activity-id", required = true) String activityId,
      @RequestHeader(value = "transaction-id", required = true) String transactionId,
      @Valid @RequestBody Customer customer) {
    String customerReqJson =
        ObjectMapperUtil.getJsonFromObj(customerPublisherDataMasker.convert(customer));
    KafkaCustomerDataRequest customerRequest = customePublisherDataConverter.convert(customer);
    logger.info("customer request:{}", customerReqJson);
    PublisherRequest customerPublisherRequest =
        new PublisherRequest(customerRequest, transactionId, activityId);
    CustomerResponse response = service.publishCustomerData(customerPublisherRequest);
    logger.info("response:{}", ObjectMapperUtil.getJsonFromObj(response));

    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
