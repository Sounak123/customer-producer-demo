package com.learn.pkg.service;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.learn.pkg.adapter.ProducerAdapter;
import com.learn.pkg.converter.CustomerDataConverter;
import com.learn.pkg.exception.ServiceException;
import com.learn.pkg.model.Customer;
import com.learn.pkg.model.Customer.CustomerStatusEnum;
import com.learn.pkg.model.CustomerAddress;
import com.learn.pkg.model.CustomerResponse;
import com.learn.pkg.model.CustomerResponse.StatusEnum;
import com.learn.pkg.model.kafka.PublisherRequest;

@ExtendWith(MockitoExtension.class)
public class PublisherServiceImplTest {

  @InjectMocks PublisherServiceImpl publisherService;

  @Mock(answer = Answers.RETURNS_MOCKS)
  private ProducerAdapter producer;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testPublishCustomerData() {

    CustomerResponse expectedCustomerResponse = new CustomerResponse();
    expectedCustomerResponse.setStatus(StatusEnum.SUCCESS);
    Mockito.when(producer.send(Mockito.any())).thenReturn(expectedCustomerResponse);
    CustomerResponse customerResponse =
        publisherService.publishCustomerData(getCustomerPublisherData());
    assertEquals("success", customerResponse.getStatus().toString());
  }

  @Test
  public void testPublishCustomerDataFailure() {
    ServiceException ex = new ServiceException("Failed to execute operation");
    Mockito.doThrow(ex).when(producer).send(Mockito.any());

    assertThatExceptionOfType(ServiceException.class)
        .isThrownBy(
            () -> {
              publisherService.publishCustomerData(getCustomerPublisherData());
            })
        .withMessage("Failed to execute operation");
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
