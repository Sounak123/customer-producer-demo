package com.learn.pkg.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.apache.kafka.common.errors.TimeoutException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.pkg.adapter.ProducerAdapter;
import com.learn.pkg.exception.CustomerControllerAdvice;
import com.learn.pkg.model.Customer;
import com.learn.pkg.model.Customer.CustomerStatusEnum;
import com.learn.pkg.model.CustomerAddress;
import com.learn.pkg.service.PublisherService;
import com.learn.pkg.service.PublisherServiceImpl;
import com.learn.pkg.util.MaskerUtils;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {

  @Mock private KafkaTemplate<String, Customer> kafkaTemplate;

  @InjectMocks private CustomerController customerController = new CustomerController();

  private MockMvc mockMvc;
  private ProducerAdapter adapter;

  @BeforeEach
  public void init() {
    PublisherService service = new PublisherServiceImpl();
    adapter = new ProducerAdapter();
    ReflectionTestUtils.setField(adapter, "topic", "xyz");
    ReflectionTestUtils.setField(adapter, "kafkaTemplate", kafkaTemplate);
    ReflectionTestUtils.setField(adapter, "masker", new MaskerUtils());
    ReflectionTestUtils.setField(service, "producer", adapter);
    ReflectionTestUtils.setField(customerController, "masker", new MaskerUtils());
    ReflectionTestUtils.setField(customerController, "service", service);
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
            post("/v1/customers/add_customer_data")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(getCustomerData())
                .headers(buildHttpHeaders()))
        .andExpect(MockMvcResultMatchers.status().is(200));
  }

  @Test
  public void testAddCustomerDataFailure() throws Exception {

    Mockito.when(kafkaTemplate.send(Mockito.anyString(), Mockito.any()))
        .thenThrow(TimeoutException.class);

    ReflectionTestUtils.setField(adapter, "kafkaTemplate", kafkaTemplate);
    mockMvc
        .perform(
            post("/v1/customers/add_customer_data")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(getCustomerData())
                .headers(buildHttpHeaders()))
        .andExpect(MockMvcResultMatchers.status().is(400));
  }

  private String getCustomerData() throws JsonProcessingException {
    Customer c = new Customer();
    c.setCustomerNumber("C000000004");
    c.setFirstName("Ronald");
    c.setLastName("Wesley");
    c.setBirthdate("26-12-2010");
    c.setCountry("USA");
    c.setCountry("US");
    c.setMobileNumber("9083618912");
    c.setEmail("user@example.com");
    c.customerStatus(CustomerStatusEnum.RESTORED);

    CustomerAddress address = new CustomerAddress();
    address.addressLine1("3/1 XYZ avenue,");
    address.addressLine2("Boston, USA");
    address.street("Storrow Dr road");
    address.postalCode("02215");

    c.address(address);

    return mapToJson(c);
  }

  private HttpHeaders buildHttpHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-Type", "application/json");
    headers.set("Authorization", "bearer 5b2713db-00a8-491d-ae72-3b9f5fafa6ba");
    headers.set("Activity-Id", "customer-transaction");
    headers.set("Application-Id", "customer-producer-demo");
    return headers;
  }

  protected String mapToJson(Object obj) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(obj);
  }
}