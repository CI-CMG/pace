package edu.colorado.cires.pace.data.translator;

import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import java.util.UUID;
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

  @Override
  public ObjectWithUniqueField setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  @Override
  public SensorTranslator setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}
