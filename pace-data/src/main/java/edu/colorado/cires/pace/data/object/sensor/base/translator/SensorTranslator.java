package edu.colorado.cires.pace.data.object.sensor.base.translator;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.base.Translator;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * SensorTranslator extends Translator and holds sensorUUID, sensorName,
 * description, and id
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public class SensorTranslator extends Translator {
  private String sensorUUID;
  private String sensorName;
  private String description;
  private String id;

  /**
   * Returns a new object with the provided uuid
   *
   * @param uuid field for assigning uuid to new object
   * @return AbstractObject with the provided uuid
   */
  @Override
  public AbstractObject setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  /**
   * Returns a new translator with the provided visibility
   *
   * @param visible boolean which indicates whether to make the object visible
   *                or invisible
   * @return SensorTranslator with the provided visibility
   */
  @Override
  public SensorTranslator setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}
