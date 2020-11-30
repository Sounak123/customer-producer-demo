package com.learn.pkg.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.pkg.model.Customer;
import com.learn.pkg.model.Customer.CustomerStatusEnum;
import com.learn.pkg.model.CustomerAddress;

@ExtendWith(MockitoExtension.class)
public class MaskerUtilsTest {

  @InjectMocks private MaskerUtils masker;

  @Test
  public void testMask() throws JsonParseException, JsonMappingException, IOException {
    Customer maskedData = mapFromJson(masker.mask(getCustomerData()), Customer.class);
    assertEquals("C00000****", maskedData.getCustomerNumber());
    assertEquals("XX-XX-2010", maskedData.getBirthdate());
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
    address.postalCode("02215");

    c.address(address);

    return c;
  }

  protected <T> T mapFromJson(String json, Class<T> clazz)
      throws JsonParseException, JsonMappingException, IOException {

    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readValue(json, clazz);
  }
}
