package com.learn.pkg.constants;

public enum MaskEnum {
  DOB_MASK_PATTERN("^.{0,5}"),
  CUSTOMER_NUMBER_MASK_PATTERN(".{4}$"),
  EMAIL_MASK_PATTERN("^.{0,4}"),
  DOB_REPLACEMENT("XX-XX"),
  REPLACEMENT("****");

  private String value;

  private MaskEnum(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
