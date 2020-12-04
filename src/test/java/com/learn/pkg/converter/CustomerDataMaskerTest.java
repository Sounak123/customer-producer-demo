package com.learn.pkg.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.learn.pkg.model.Customer;
import com.learn.pkg.model.Customer.CustomerStatusEnum;
import com.learn.pkg.model.CustomerAddress;
import com.learn.pkg.model.kafka.KafkaCustomerDataRequest;

@ExtendWith(MockitoExtension.class)
public class CustomerDataMaskerTest {

  @InjectMocks private CustomerDataMasker customerPublisherDataMasker;

  @BeforeEach
  public void init() {
    CustomerDataConverter customerDataConverter = new CustomerDataConverter();
    ReflectionTestUtils.setField(
        customerPublisherDataMasker, "customerDataConverter", customerDataConverter);
  }

  @Test
  public void testConvert() {
    KafkaCustomerDataRequest maskedData = customerPublisherDataMasker.convert(getCustomerData());
    assertEquals("C00000****", maskedData.getCustomerNumber());
    assertEquals("XX-XX-2010", maskedData.getBirthdate());
    assertEquals("****@example.com", maskedData.getEmail());
  }

  public static Customer getCustomerData() {
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
    address.postalCode("702215");

    c.address(address);

    return c;
  }
}
