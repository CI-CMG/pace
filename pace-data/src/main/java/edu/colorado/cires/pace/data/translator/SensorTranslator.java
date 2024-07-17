package edu.colorado.cires.pace.data.translator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public class SensorTranslator extends Translator {
  private String sensorUUID;
  private String sensorName;
  private String description;
  private PositionTranslator positionTranslator;
}
