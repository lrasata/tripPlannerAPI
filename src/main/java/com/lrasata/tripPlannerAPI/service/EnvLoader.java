package com.lrasata.tripPlannerAPI.service;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvLoader {

  private static Dotenv dotenv;

  public static void load() {
    String environment = System.getenv("ENVIRONMENT");
    if ("local".equalsIgnoreCase(environment)) {
      try {
        dotenv =
            Dotenv.configure()
                .ignoreIfMissing() // don’t throw error if .env is missing
                .load();
        System.out.println(".env loaded for local environment");

        // Make Dotenv vars visible to Spring Boot
        dotenv
            .entries()
            .forEach(
                entry -> {
                  System.setProperty(entry.getKey(), entry.getValue());
                  // System.out.println("Loaded: " + entry.getKey());
                });
      } catch (Exception e) {
        System.out.println("Failed to load .env locally: " + e.getMessage());
      }
    } else {
      System.out.println("Not local environment; skipping .env load. ENVIRONMENT=" + environment);
    }
  }

  // Optional helper to get a variable
  public static String get(String key) {
    if (dotenv != null) {
      return dotenv.get(key);
    } else {
      return System.getenv(key);
    }
  }
}
