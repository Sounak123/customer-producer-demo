package com.learn.pkg.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.concurrent.ListenableFuture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.pkg.adapter.ProducerAdapter;
import com.learn.pkg.converter.CustomerDataConverter;
import com.learn.pkg.converter.CustomerDataMasker;
import com.learn.pkg.exception.CustomerControllerAdvice;
import com.learn.pkg.model.Customer;
import com.learn.pkg.model.Customer.CustomerStatusEnum;
import com.learn.pkg.model.CustomerAddress;
import com.learn.pkg.model.kafka.PublisherRequest;
import com.learn.pkg.service.PublisherService;
import com.learn.pkg.service.PublisherServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {

  private static final String TEST_URI = "/v1/customers/add-customer-data";

  @Mock(answer = Answers.RETURNS_MOCKS)
  private KafkaTemplate<String, PublisherRequest> kafkaTemplate;

  @InjectMocks private CustomerController customerController = new CustomerController();

  private MockMvc mockMvc;
  private ProducerAdapter adapter;

  @BeforeEach
  public void init() {
    PublisherService service = new PublisherServiceImpl();
    adapter = new ProducerAdapter();
    CustomerDataMasker masker = new CustomerDataMasker();
    ReflectionTestUtils.setField(masker, "customerDataConverter", new CustomerDataConverter());
    ReflectionTestUtils.setField(adapter, "topic", "xyz");
    ReflectionTestUtils.setField(adapter, "kafkaTemplate", kafkaTemplate);
    ReflectionTestUtils.setField(adapter, "customerPublisherDataMasker", masker);
    ReflectionTestUtils.setField(service, "producer", adapter);
    ReflectionTestUtils.setField(customerController, "customerPublisherDataMasker", masker);
    ReflectionTestUtils.setField(customerController, "service", service);
    ReflectionTestUtils.setField(
        customerController, "customePublisherDataConverter", new CustomerDataConverter());
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
  public void testAddCustomerDataFailure() throws Exception {

    ListenableFuture<SendResult<String, PublisherRequest>> future =
        Mockito.mock(ListenableFuture.class);
    Mockito.when(kafkaTemplate.send(Mockito.anyString(), Mockito.any())).thenReturn(future);

    Mockito.doThrow(InterruptedException.class).when(future).get();

    ReflectionTestUtils.setField(adapter, "kafkaTemplate", kafkaTemplate);
    mockMvc
        .perform(
            post(TEST_URI)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(getCustomerData())
                .headers(buildHttpHeaders()))
        .andExpect(MockMvcResultMatchers.status().is(503));
  }

  private String getCustomerData() throws JsonProcessingException {
    Customer c = new Customer();
    c.setCustomerNumber("C000000004");
    c.setFirstName("Ronald");
    c.setLastName("Wesley");
    c.setBirthdate("26-12-2010");
    c.setCountry("USA");
    c.setCountryCode("US");
    c.setMobileNumber("9083618912");
    c.setEmail("user@example.com");
    c.customerStatus(CustomerStatusEnum.RESTORED);

    CustomerAddress address = new CustomerAddress();
    address.addressLine1("3/1 XYZ avenue,");
    address.addressLine2("Boston, USA");
    address.street("Storrow Dr road");
    address.postalCode("702215");

    c.address(address);

    return mapToJson(c);
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
