package com.lrasata.tripPlannerAPI;

import com.lrasata.tripPlannerAPI.service.EnvLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TripPlannerAPIApplication {

  public static void main(String[] args) {
    EnvLoader.load();
    SpringApplication.run(TripPlannerAPIApplication.class, args);
  }
}
