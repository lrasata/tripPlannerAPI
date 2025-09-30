package com.lrasata.tripPlannerAPI.service;

import com.lrasata.tripPlannerAPI.service.dto.UserFileMetadataDTO;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;

@Service
public class UserExtraInfoService {

  private final DynamoDbClient dynamoDbClient;

  @Value("${aws.dynamodb.table.user-extra-info}")
  private String tableName;

  public UserExtraInfoService(DynamoDbClient dynamoDbClient) {
    this.dynamoDbClient = dynamoDbClient;
  }

  public List<UserFileMetadataDTO> getFilesForUser(Long userId) {
    QueryRequest request =
        QueryRequest.builder()
            .tableName(tableName)
            .keyConditionExpression("user_id = :uid")
            .expressionAttributeValues(
                Map.of(":uid", AttributeValue.builder().s(userId.toString()).build()))
            .build();

    QueryResponse response = dynamoDbClient.query(request);

    return response.items().stream()
        .map(
            item -> {
              UserFileMetadataDTO metadata = new UserFileMetadataDTO();
              metadata.setUserId(item.get("user_id").s());
              metadata.setFileKey(item.get("file_key").s());
              metadata.setThumbnailKey(item.get("thumbnail_key").s());
              return metadata;
            })
        .toList();
  }
}
