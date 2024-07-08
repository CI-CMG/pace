package edu.colorado.cires.pace.data.translator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder(toBuilder = true)
@Jacksonized
public class DepthSensorTranslator extends SensorTranslator {

  public static DepthSensorTranslatorBuilder<?, ?> toBuilder(SensorTranslator sensorTranslator) {
    return DepthSensorTranslator.builder()
        .uuid(sensorTranslator.getUuid())
        .name(sensorTranslator.getName())
        .sensorUUID(sensorTranslator.getSensorUUID())
        .sensorName(sensorTranslator.getSensorName())
        .description(sensorTranslator.getDescription())
        .positionTranslator(sensorTranslator.getPositionTranslator());
  }
}
