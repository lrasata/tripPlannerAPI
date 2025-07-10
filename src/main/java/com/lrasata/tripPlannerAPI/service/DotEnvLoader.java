package com.lrasata.tripPlannerAPI.service;

import io.github.cdimascio.dotenv.Dotenv;

public class DotEnvLoader {
  public static void load() {
    Dotenv dotenv = Dotenv.load();
    dotenv
        .entries()
        .forEach(
            entry -> {
              // set as system property so Spring can pick it up
              System.setProperty(entry.getKey(), entry.getValue());
            });
  }
}
