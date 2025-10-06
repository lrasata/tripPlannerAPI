package com.lrasata.tripPlannerAPI.service.dto;

public class TripMetadataDTO {
  private String tripId;
  private String fileKey;
  private String thumbnailKey;

  public String getTripId() {
    return tripId;
  }

  public void setTripId(String userId) {
    this.tripId = userId;
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
