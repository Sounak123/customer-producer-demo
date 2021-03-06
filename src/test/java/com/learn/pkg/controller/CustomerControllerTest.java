package com.learn.pkg.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.pkg.converter.CustomerDataConverter;
import com.learn.pkg.converter.KafkaCustomerDataRequestConverter;
import com.learn.pkg.exception.CustomerControllerAdvice;
import com.learn.pkg.model.Customer;
import com.learn.pkg.model.Customer.CustomerStatusEnum;
import com.learn.pkg.model.CustomerAddress;
import com.learn.pkg.service.PublisherService;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {
  private static final String TEST_URI = "/v1/customers/add-customer-data";

  @InjectMocks private CustomerController customerController;

  @Mock private KafkaCustomerDataRequestConverter customerPublisherDataMasker;

  @Mock private CustomerDataConverter customePublisherDataConverter;

  @Mock private PublisherService service;

  private MockMvc mockMvc;

  @BeforeEach
  public void init() {
    MockitoAnnotations.initMocks(this);
    mockMvc =
        MockMvcBuilders.standaloneSetup(customerController)
            .setControllerAdvice(new CustomerControllerAdvice())
            .build();
  }

  @Test
  public void testAddCustomerData() throws Exception {

    mockMvc
        .perform(
            post(TEST_URI)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(getCustomerData())
                .headers(buildHttpHeaders()))
        .andExpect(MockMvcResultMatchers.status().is(200));
  }

  @Test
  public void testAddCustomerInvalidData() throws Exception {

    mockMvc
        .perform(
            post(TEST_URI)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(new Customer()))
                .headers(buildHttpHeaders()))
        .andExpect(MockMvcResultMatchers.status().is(400));
  }

  @Test
  public void testAddCustomerInvalidURL() throws Exception {

    mockMvc
        .perform(
            post(TEST_URI + "/v2")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(getCustomerData())
                .headers(buildHttpHeaders()))
        .andExpect(MockMvcResultMatchers.status().is(404));
  }

  private String getCustomerData() throws JsonProcessingException {
    Customer customer = new Customer();
    customer.setCustomerNumber("C000000004");
    customer.setFirstName("Ronald");
    customer.setLastName("Wesley");
    customer.setBirthdate("26-12-2010");
    customer.setCountry("USA");
    customer.setCountryCode("US");
    customer.setMobileNumber("9083618912");
    customer.setEmail("user@example.com");
    customer.customerStatus(CustomerStatusEnum.RESTORED);

    CustomerAddress address = new CustomerAddress();
    address.addressLine1("3/1 XYZ avenue,");
    address.addressLine2("Boston, USA");
    address.street("Storrow Dr road");
    address.postalCode("702215");

    customer.address(address);

    return mapToJson(customer);
  }

  private HttpHeaders buildHttpHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-Type", "application/json");
    headers.set("Authorization", "bearer 5b2713db-00a8-491d-ae72-3b9f5fafa6ba");
    headers.set("activity-Id", "customer-transaction");
    headers.set("transaction-Id", "customer-producer-demo");
    return headers;
  }

  protected String mapToJson(Object obj) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(obj);
  }
}
