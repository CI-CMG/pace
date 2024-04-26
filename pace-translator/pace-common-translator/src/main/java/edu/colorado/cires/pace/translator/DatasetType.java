package edu.colorado.cires.pace.translator;

enum DatasetType {
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
}
