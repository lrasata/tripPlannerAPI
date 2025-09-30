package com.lrasata.tripPlannerAPI.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class DynamoDbConfig {

  @Value("${aws.dynamodb.region}")
  private String dynamoRegion;

  @Bean
  public DynamoDbClient dynamoDbClient() {
    return DynamoDbClient.builder().region(Region.of(dynamoRegion)).build();
  }
}
