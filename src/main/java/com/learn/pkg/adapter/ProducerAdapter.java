package com.learn.pkg.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.learn.pkg.model.Customer;
import com.learn.pkg.util.MaskerUtils;

@Component
public class ProducerAdapter {
  private static final Logger logger = LoggerFactory.getLogger(ProducerAdapter.class);
  @Autowired private KafkaTemplate<String, Customer> kafkaTemplate;

  @Autowired private MaskerUtils masker;

  @Value("${cloudkarafka.topic}")
  private String topic;

  public void send(Customer message) {
    this.kafkaTemplate.send(topic, message);
    logger.info("Sent data [" + masker.mask(message) + "] to " + topic);
  }
}
