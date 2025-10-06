package com.lrasata.tripPlannerAPI.rest.controller;

import com.lrasata.tripPlannerAPI.entity.User;
import com.lrasata.tripPlannerAPI.service.TripService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@RestController
@RequestMapping("/api")
public class TripMetadataController {

  private static final Logger LOG = LoggerFactory.getLogger(TripMetadataController.class);

  private final TripService tripService;

  @Value("${aws.s3.bucket.name}")
  private String s3BucketName;

  public TripMetadataController(TripService tripService) {
    this.tripService = tripService;
  }

  // TODO check in cloudfront what should be the path here
  @GetMapping("/{type:(?:uploads|thumbnails)}/{tripId}/{fileName}")
  public void getTripImage(
      @PathVariable String type,
      @PathVariable String tripId,
      @PathVariable String fileName,
      Authentication authentication,
      HttpServletResponse response) {

    LOG.debug("REST request to get file type={}, tripId={}, filename={}", type, tripId, fileName);

    if (authentication == null
        || !authentication.isAuthenticated()
        || authentication.getPrincipal().equals("anonymousUser")) {
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      return;
    }

    User currentUser = (User) authentication.getPrincipal();

    // Construct the S3 key
    String s3Key = type + "/" + tripId + "/" + fileName;

    // Check if current user is participant of the trip OR SUPER_ADMIN OR is admin of the trip
    if (!tripService.checkIfUserCanAccessTrip(Long.valueOf(tripId), currentUser)) {
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      return;
    }

    // Fetch and stream from S3
    try (S3Client s3 = S3Client.create()) {
      GetObjectRequest getObjectRequest =
          GetObjectRequest.builder().bucket(s3BucketName).key(s3Key).build();

      ResponseInputStream<GetObjectResponse> s3Object = s3.getObject(getObjectRequest);
      response.setContentType("image/*");

      // Stream directly to the client
      IOUtils.copy(s3Object, response.getOutputStream());
      response.flushBuffer();
    } catch (Exception e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }
}
