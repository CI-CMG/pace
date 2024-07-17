package edu.colorado.cires.pace.translator;

import edu.colorado.cires.pace.data.object.AudioPackage;
import edu.colorado.cires.pace.data.object.CPODPackage;
import edu.colorado.cires.pace.data.object.DetectionsPackage;
import edu.colorado.cires.pace.data.object.Package;
import edu.colorado.cires.pace.data.object.SoundClipsPackage;
import edu.colorado.cires.pace.data.object.SoundLevelMetricsPackage;
import edu.colorado.cires.pace.data.object.SoundPropagationModelsPackage;
import java.util.Arrays;
import java.util.List;
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
  
  public static DatasetType fromPackage(Package dataPackage) {
    return switch (dataPackage.getClass().getSimpleName()) {
      case "AudioPackage" -> AUDIO;
      case "CPODPackage" -> CPOD;
      case "DetectionsPackage" -> DETECTIONS;
      case "SoundClipsPackage" -> SOUND_CLIPS;
      case "SoundLevelMetricsPackage" -> SOUND_LEVEL_METRICS;
      case "SoundPropagationModelsPackage" -> SOUND_PROPAGATION_MODELS;
      default -> throw new IllegalArgumentException(String.format(
          "Invalid dataset type: %s. Was not one of: %s",
          dataPackage.getClass().getSimpleName(),
          String.join(", ", List.of(
              AudioPackage.class.getSimpleName(),
              CPODPackage.class.getSimpleName(),
              DetectionsPackage.class.getSimpleName(),
              SoundClipsPackage.class.getSimpleName(),
              SoundLevelMetricsPackage.class.getSimpleName(),
              SoundPropagationModelsPackage.class.getSimpleName()
          ))
      ));
    };
  } 
}
