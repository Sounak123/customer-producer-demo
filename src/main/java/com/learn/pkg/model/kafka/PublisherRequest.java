package com.learn.pkg.model.kafka;

public class PublisherRequest {
  private KafkaCustomerDataRequest customerData;
  private String transactionId;
  private String activityId;

  public PublisherRequest(
      KafkaCustomerDataRequest customerData, String transactionId, String activityId) {
    this.customerData = customerData;
    this.transactionId = transactionId;
    this.activityId = activityId;
  }

  public KafkaCustomerDataRequest getCustomerData() {
    return customerData;
  }

  public void setCustomerData(KafkaCustomerDataRequest customerData) {
    this.customerData = customerData;
  }

  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  public String getActivityId() {
    return activityId;
  }

  public void setActivityId(String activityId) {
    this.activityId = activityId;
  }
}
