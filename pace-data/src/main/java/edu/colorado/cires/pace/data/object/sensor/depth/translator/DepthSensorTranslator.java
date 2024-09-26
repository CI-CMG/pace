package edu.colorado.cires.pace.data.object.sensor.depth.translator;

import edu.colorado.cires.pace.data.object.sensor.base.translator.SensorTranslator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * DepthSensorTranslator extends SensorTranslator and provides specific builder
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder(toBuilder = true)
@Jacksonized
public class DepthSensorTranslator extends SensorTranslator {

  /**
   * Returns builder for depth sensor translator
   * @param sensorTranslator data to apply to translator
   * @return DepthSensorTranslatorBuilder with relevant data
   */
  public static DepthSensorTranslatorBuilder<?, ?> toBuilder(SensorTranslator sensorTranslator) {
    return DepthSensorTranslator.builder()
        .uuid(sensorTranslator.getUuid())
        .name(sensorTranslator.getName())
        .sensorUUID(sensorTranslator.getSensorUUID())
        .sensorName(sensorTranslator.getSensorName())
        .description(sensorTranslator.getDescription());
  }
}
