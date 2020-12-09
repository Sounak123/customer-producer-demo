package com.learn.pkg.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.pkg.converter.KafkaCustomerDataRequestConverterTest;

@ExtendWith(MockitoExtension.class)
public class ObjectMapperUtilTest {

  @Mock ObjectMapper objMapper;

  @Test
  public void testGetJsonFromObj() {
    String strObj = ObjectMapperUtil.getJsonFromObj(KafkaCustomerDataRequestConverterTest.getCustomerData());
    assertEquals(
        "{\"customerNumber\":\"C000000004\",\"firstName\":\"Ronald\",\"lastName\":\"Wesley\",\"birthdate\":\"26-12-2010\",\"country\":\"US\",\"countryCode\":null,\"mobileNumber\":\"9083618912\",\"email\":\"user@example.com\",\"customerStatus\":\"RESTORED\",\"address\":{\"addressLine1\":\"3/1 XYZ avenue,\",\"addressLine2\":\"Boston, USA\",\"street\":\"Storrow Dr road\",\"postalCode\":\"702215\"}}",
        strObj);
  }
}
