package com.learn.pkg.util;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.stereotype.Component;

import com.learn.pkg.model.Customer;
import com.learn.pkg.model.CustomerAddress;

@Component
public class MaskerUtils {
  private static final String MASK = "****";

  public String mask(Customer customerObject) {
    Customer customer = new Customer();
    CustomerAddress address = new CustomerAddress();
    address.setAddressLine1(customerObject.getAddress().getAddressLine1());
    address.setAddressLine2(customerObject.getAddress().getAddressLine2());
    address.setPostalCode(customerObject.getAddress().getPostalCode());
    address.setStreet(customerObject.getAddress().getStreet());

    customer.setAddress(address);
    customer.setMobileNumber(customerObject.getMobileNumber());
    customer.setBirthdate(getMaskedDob(customerObject.getBirthdate())); // Mask
    customer.setCountry(customerObject.getCountry());
    customer.setCountryCode(customerObject.getCountryCode());
    customer.setCustomerNumber(
        customerObject
                .getCustomerNumber()
                .substring(0, customerObject.getCustomerNumber().length() - 4)
            + MASK); // Mask
    customer.setCustomerStatus(customerObject.getCustomerStatus());
    customer.setEmail(MASK + customerObject.getEmail().substring(4)); // Mask
    customer.setFirstName(customerObject.getFirstName());
    customer.setLastName(customerObject.getLastName());

    return ObjectMapperUtil.getJsonFromObj(customer);
  }

  private String getMaskedDob(
      @NotNull
          @Pattern(
              regexp = "/^([0-2][0-9]|(3)[0-1]|(XX))(-)(((0)[0-9])|((1)[0-2])|(XX))(-)\\d{4}$/i")
          String birthdate) {
    return "XX-XX-" + birthdate.substring(6);
  }
}
