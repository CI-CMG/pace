package edu.colorado.cires.pace.translator;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum DatasetType {
  SOUND_CLIPS("sound clips"),
  AUDIO("audio"),
  CPOD("CPOD"),
  DETECTIONS("detections"),
  SOUND_LEVEL_METRICS("sound level metrics"),
  SOUND_PROPAGATION_MODELS("sound propagation models");

  private final String name;

  DatasetType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
  
  public static DatasetType fromName(String name) {
    return switch (name) {
      case "sound clips" -> SOUND_CLIPS;
      case "audio" -> AUDIO;
      case "CPOD" -> CPOD;
      case "detections" -> DETECTIONS;
      case "sound level metrics" -> SOUND_LEVEL_METRICS;
      case "sound propagation models" -> SOUND_PROPAGATION_MODELS;
      default -> throw new IllegalArgumentException(String.format(
          "Invalid dataset type: %s. Was not one of: %s",
          name,
          Arrays.stream(DatasetType.values())
              .map(DatasetType::getName)
              .collect(Collectors.joining(", "))
      ));
    };
  }
}
