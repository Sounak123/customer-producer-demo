package com.learn.pkg.converter;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.learn.pkg.model.kafka.KafkaCustomerDataRequest;

@ExtendWith(MockitoExtension.class)
public class CustomerDataConverterTest {

  @InjectMocks private CustomerDataConverter customerDataConverter;

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testConvert() {
    KafkaCustomerDataRequest customerData =
        customerDataConverter.convert(KafkaCustomerDataRequestConverterTest.getCustomerData());
    assertEquals("C000000004", customerData.getCustomerNumber());
  }
}
