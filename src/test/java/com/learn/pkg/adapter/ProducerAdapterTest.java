package com.learn.pkg.adapter;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertEquals;

import org.apache.kafka.common.errors.TimeoutException;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import com.learn.pkg.converter.CustomerDataConverter;
import com.learn.pkg.converter.KafkaCustomerDataRequestConverter;
import com.learn.pkg.exception.ServiceException;
import com.learn.pkg.model.Customer;
import com.learn.pkg.model.Customer.CustomerStatusEnum;
import com.learn.pkg.model.CustomerAddress;
import com.learn.pkg.model.CustomerResponse;
import com.learn.pkg.model.kafka.PublisherRequest;

@ExtendWith(MockitoExtension.class)
public class ProducerAdapterTest {

  @InjectMocks private ProducerAdapter producerAdapter;

  @Mock(answer = Answers.RETURNS_MOCKS)
  private KafkaTemplate<String, PublisherRequest> kafkaTemplate;

  @Mock private KafkaCustomerDataRequestConverter customerPublisherDataMasker;

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testSend() {
    CustomerResponse customerResponse = producerAdapter.send(getCustomerPublisherData());
    assertEquals("success", customerResponse.getStatus().toString());
  }

  @Test
  public void testSendFailure() {
    Mockito.doThrow(new TimeoutException("Timed out!"))
        .when(kafkaTemplate)
        .send(Mockito.any(), Mockito.any());

    assertThatExceptionOfType(ServiceException.class)
        .isThrownBy(
            () -> {
              producerAdapter.send(getCustomerPublisherData());
            })
        .withMessage("Timed out!");
  }

  private PublisherRequest getCustomerPublisherData() {
    PublisherRequest publisherRequest;
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
    publisherRequest =
        new PublisherRequest(
            new CustomerDataConverter().convert(customer), "transaction-id", "activity-id");
    return publisherRequest;
  }
}
