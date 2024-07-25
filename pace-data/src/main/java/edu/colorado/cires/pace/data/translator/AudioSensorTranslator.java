package edu.colorado.cires.pace.data.translator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder(toBuilder = true)
@Jacksonized
public class AudioSensorTranslator extends SensorTranslator {
  
  private final String hydrophoneId;
  private final String preampId;
  
  public static AudioSensorTranslatorBuilder<?, ?> toBuilder(SensorTranslator sensorTranslator) {
    return AudioSensorTranslator.builder()
        .uuid(sensorTranslator.getUuid())
        .name(sensorTranslator.getName())
        .sensorUUID(sensorTranslator.getSensorUUID())
        .sensorName(sensorTranslator.getSensorName())
        .description(sensorTranslator.getDescription());
  }

}
