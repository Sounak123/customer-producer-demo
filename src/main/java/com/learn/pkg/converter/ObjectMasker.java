package com.learn.pkg.converter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.stereotype.Component;

import com.learn.pkg.model.Customer;
import com.learn.pkg.model.CustomerAddress;

@Component
public class ObjectMasker implements Converter<Customer> {

  private static final String MASK = "****";

  @Override
  public Customer convert(Customer object) {
    Customer customer = new Customer();
    CustomerAddress address = new CustomerAddress();
    address.setAddressLine1(object.getAddress().getAddressLine1());
    address.setAddressLine2(object.getAddress().getAddressLine2());
    address.setPostalCode(object.getAddress().getPostalCode());
    address.setStreet(object.getAddress().getStreet());

    customer.setAddress(address);
    customer.setMobileNumber(object.getMobileNumber());
    customer.setBirthdate(getMaskedDob(object.getBirthdate())); // Mask
    customer.setCountry(object.getCountry());
    customer.setCountryCode(object.getCountryCode());
    customer.setCustomerNumber(
        object.getCustomerNumber().substring(0, object.getCustomerNumber().length() - 4)
            + MASK); // Mask
    customer.setCustomerStatus(object.getCustomerStatus());
    customer.setEmail(MASK + object.getEmail().substring(4)); // Mask
    customer.setFirstName(object.getFirstName());
    customer.setLastName(object.getLastName());

    return customer;
  }

  private String getMaskedDob(
      @NotNull
          @Pattern(regexp = "^([0-2][0-9]|(3)[0-1]|(XX))(-)(((0)[0-9])|((1)[0-2])|(XX))(-)\\d{4}$")
          String birthdate) {
    return "XX-XX-" + birthdate.substring(6);
  }
}
