package com.learn.pkg.converter;

import org.springframework.stereotype.Component;

import com.learn.pkg.model.Customer;
import com.learn.pkg.model.kafka.KafkaCustomerAddress;
import com.learn.pkg.model.kafka.KafkaCustomerDataRequest;

@Component
public class CustomerDataConverter implements Converter<Customer, KafkaCustomerDataRequest> {

  @Override
  public KafkaCustomerDataRequest convert(Customer object) {
    KafkaCustomerDataRequest customer = new KafkaCustomerDataRequest();
    KafkaCustomerAddress address = new KafkaCustomerAddress();
    address.setAddressLine1(object.getAddress().getAddressLine1());
    address.setAddressLine2(object.getAddress().getAddressLine2());
    address.setPostalCode(object.getAddress().getPostalCode());
    address.setStreet(object.getAddress().getStreet());

    customer.setAddress(address);
    customer.setMobileNumber(object.getMobileNumber());
    customer.setBirthdate(object.getBirthdate());
    customer.setCountry(object.getCountry());
    customer.setCountryCode(object.getCountryCode());
    customer.setCustomerNumber(object.getCustomerNumber());
    customer.setCustomerStatus(object.getCustomerStatus().toString());
    customer.setEmail(object.getEmail());
    customer.setFirstName(object.getFirstName());
    customer.setLastName(object.getLastName());

    return customer;
  }
}
