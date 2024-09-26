package edu.colorado.cires.pace.data.object.sensor.other.translator;

import edu.colorado.cires.pace.data.object.sensor.base.translator.SensorTranslator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * OtherSensorTranslator extends SensorTranslator, holds sensorType and
 * properties, and provides builder for translator
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder(toBuilder = true)
@Jacksonized
public class OtherSensorTranslator extends SensorTranslator {
  
  private final String sensorType;
  private final String properties;

  /**
   * Returns builder for other sensor translator
   * @param sensorTranslator data to include in translator
   * @return OtherSensorTranslatorBuilder with provided data
   */
  public static OtherSensorTranslatorBuilder<?, ?> toBuilder(SensorTranslator sensorTranslator) {
    return OtherSensorTranslator.builder()
        .uuid(sensorTranslator.getUuid())
        .name(sensorTranslator.getName())
        .sensorUUID(sensorTranslator.getSensorUUID())
        .sensorName(sensorTranslator.getSensorName())
        .description(sensorTranslator.getDescription());
  }
}
