package com.lrasata.tripPlannerAPI.service;

import com.lrasata.tripPlannerAPI.service.dto.TripMetadataDTO;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;

@Service
public class TripMetadataService {

  private final DynamoDbClient dynamoDbClient;

  @Value("${aws.dynamodb.table.user-extra-info}")
  private String tableName;

  public TripMetadataService(DynamoDbClient dynamoDbClient) {
    this.dynamoDbClient = dynamoDbClient;
  }

  public List<TripMetadataDTO> getFileByTrip(Long tripId) {
    QueryRequest request =
        QueryRequest.builder()
            .tableName(tableName)
            .keyConditionExpression("trip_id = :uid")
            .expressionAttributeValues(
                Map.of(":uid", AttributeValue.builder().s(tripId.toString()).build()))
            .build();

    QueryResponse response = dynamoDbClient.query(request);

    return response.items().stream()
        .map(
            item -> {
              TripMetadataDTO metadata = new TripMetadataDTO();
              metadata.setTripId(item.get("trip_id").s());
              metadata.setFileKey(item.get("file_key").s());
              metadata.setThumbnailKey(item.get("thumbnail_key").s());
              return metadata;
            })
        .toList();
  }
}
