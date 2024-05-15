package edu.colorado.cires.pace.translator;

import edu.colorado.cires.pace.data.object.Sensor;

public enum SensorType {
  audio, depth, other;

  SensorType() {
  }

  public static SensorType fromSensor(Sensor sensor) {
    return switch (sensor.getClass().getSimpleName()) {
      case "AudioSensor" -> audio;
      case "DepthSensor" -> depth;
      case "OtherSensor" -> other;
      default -> null;
    };
  }
}
