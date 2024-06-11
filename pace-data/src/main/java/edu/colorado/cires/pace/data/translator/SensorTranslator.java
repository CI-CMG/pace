package edu.colorado.cires.pace.data.translator;

import java.util.UUID;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
public class SensorTranslator implements Translator {
  
  private final UUID uuid;
  private final String name;
  
  private String sensorUUID;
  private String sensorName;
  private String description;
  private PositionTranslator positionTranslator;

}
