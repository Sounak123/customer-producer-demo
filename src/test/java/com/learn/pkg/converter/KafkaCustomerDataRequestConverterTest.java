package com.learn.pkg.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.learn.pkg.model.Customer;
import com.learn.pkg.model.Customer.CustomerStatusEnum;
import com.learn.pkg.model.CustomerAddress;
import com.learn.pkg.model.kafka.KafkaCustomerDataRequest;

@ExtendWith(MockitoExtension.class)
public class KafkaCustomerDataRequestConverterTest {

  @InjectMocks private KafkaCustomerDataRequestConverter customerPublisherDataMasker;

  @Mock(answer = Answers.CALLS_REAL_METHODS)
  private CustomerDataConverter customerDataConverter;

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testConvert() {
    KafkaCustomerDataRequest maskedData = customerPublisherDataMasker.convert(getCustomerData());
    assertEquals("C00000****", maskedData.getCustomerNumber());
    assertEquals("XX-XX-2010", maskedData.getBirthdate());
    assertEquals("****@example.com", maskedData.getEmail());
  }

  public static Customer getCustomerData() {
    Customer customer = new Customer();
    customer.setCustomerNumber("C000000004");
    customer.setFirstName("Ronald");
    customer.setLastName("Wesley");
    customer.setBirthdate("26-12-2010");
    customer.setCountry("USA");
    customer.setCountry("US");
    customer.setMobileNumber("9083618912");
    customer.setEmail("user@example.com");
    customer.customerStatus(CustomerStatusEnum.RESTORED);

    CustomerAddress address = new CustomerAddress();
    address.addressLine1("3/1 XYZ avenue,");
    address.addressLine2("Boston, USA");
    address.street("Storrow Dr road");
    address.postalCode("702215");

    customer.address(address);

    return customer;
  }
}
