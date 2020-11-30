package com.learn.pkg.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.io.IOException;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.pkg.adapter.ProducerAdapter;
import com.learn.pkg.exception.CustomerControllerAdvice;
import com.learn.pkg.model.Customer;
import com.learn.pkg.model.Customer.CustomerStatusEnum;
import com.learn.pkg.model.CustomerAddress;
import com.learn.pkg.model.ModelApiResponse;
import com.learn.pkg.model.ModelApiResponse.StatusEnum;
import com.learn.pkg.service.PublisherService;
import com.learn.pkg.service.PublisherServiceImpl;
import com.learn.pkg.util.MaskerUtils;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {

  // @Autowired private MockMvc mvc;

  // @Autowired private ProducerAdapter adapter;
  // private KafkaTemplate<String, Customer> kafkaTemplate;

  @InjectMocks private CustomerController customerController = new CustomerController();

  /*@InjectMocks private PublisherService customerService;

  @InjectMocks private MaskerUtils masker;*/

  private MockMvc mockMvc;

  @BeforeEach
  public void init() {
    PublisherService service = new PublisherServiceImpl();
    ProducerAdapter adapter = new ProducerAdapter();
    ReflectionTestUtils.setField(adapter, "topic", "xyz");
    ReflectionTestUtils.setField(
        adapter,
        "kafkaTemplate",
        new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(new HashMap<>())));
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
    /*KafkaOperations kafkaTemplate = Mockito.mock(KafkaOperations.class);
    Mockito.when(kafkaTemplate.send(anyString(), any(Customer.class)))
        .thenReturn(new SettableListenableFuture<>());*/
    ProducerAdapter adapter = Mockito.mock(ProducerAdapter.class);

    ModelApiResponse mSuccess = new ModelApiResponse();
    mSuccess.setMessage("Data Successfully Sent.");
    mSuccess.setStatus(StatusEnum.SUCCESS);

    Mockito.when(adapter.send(Mockito.any(Customer.class))).thenReturn(mSuccess);

    mockMvc
        .perform(
            post("/v1/customers/add_customer_data")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(getCustomerData())
                .headers(buildHttpHeaders()))
        .andExpect(MockMvcResultMatchers.status().is(200));
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
    headers.set("Application-Id", "retail-customer-app");
    return headers;
  }

  protected String mapToJson(Object obj) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(obj);
  }

  protected <T> T mapFromJson(String json, Class<T> clazz)
      throws JsonParseException, JsonMappingException, IOException {

    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readValue(json, clazz);
  }
}
