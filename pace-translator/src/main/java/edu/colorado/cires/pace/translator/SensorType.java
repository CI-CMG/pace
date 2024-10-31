package edu.colorado.cires.pace.translator;

import edu.colorado.cires.pace.data.object.sensor.base.Sensor;

/**
 * Provides a list of sensor types
 */
public enum SensorType {
  audio, depth, other;

  SensorType() {
  }

  /**
   * Returns sensor type of sensor
   * @param sensor sensor to check type of
   * @return SensorType of sensor
   */
  public static SensorType fromSensor(Sensor sensor) {
    return switch (sensor.getClass().getSimpleName()) {
      case "AudioSensor" -> audio;
      case "DepthSensor" -> depth;
      case "OtherSensor" -> other;
      default -> null;
    };
  }
}
