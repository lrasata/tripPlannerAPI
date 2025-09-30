package com.lrasata.tripPlannerAPI.service.dto;

public class UserFileMetadataDTO {
  private String userId;
  private String fileKey;
  private String thumbnailKey;

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getFileKey() {
    return fileKey;
  }

  public void setFileKey(String fileKey) {
    this.fileKey = fileKey;
  }

  public String getThumbnailKey() {
    return thumbnailKey;
  }

  public void setThumbnailKey(String thumbnailKey) {
    this.thumbnailKey = thumbnailKey;
  }
}
