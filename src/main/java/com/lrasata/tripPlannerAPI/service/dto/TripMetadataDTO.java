package com.lrasata.tripPlannerAPI.service.dto;

public class TripMetadataDTO {
  private String tripId;
  private String fileKey;
  private String thumbnailKey;
  private String resource;
  private Boolean selected;

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

  public String getResource() {
    return resource;
  }

  public void setResource(String resource) {
    this.resource = resource;
  }

  public Boolean getSelected() {
    return selected;
  }

  public void setSelected(Boolean selected) {
    this.selected = selected;
  }
}
