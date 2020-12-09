package com.learn.pkg.model.kafka;

public class PublisherRequest {
  private KafkaCustomerDataRequest customerData;
  private String transactionId;
  private String activityId;

  public PublisherRequest() {}

  public PublisherRequest(
      KafkaCustomerDataRequest customerData, String transactionId, String activityId) {
    this.customerData = customerData;
    this.transactionId = transactionId;
    this.activityId = activityId;
  }

  public KafkaCustomerDataRequest getCustomerData() {
    return customerData;
  }

  public String getTransactionId() {
    return transactionId;
  }

  public String getActivityId() {
    return activityId;
  }
}
