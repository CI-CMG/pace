package edu.colorado.cires.pace.data.object.sensor.other;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.sensor.base.Sensor;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * OtherSensor extends Sensor and holds sensorType and properties
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class OtherSensor extends Sensor {
  @NotBlank
  private String sensorType;
  @NotBlank
  private String properties;

  /**
   * Returns a new object with the specified uuid
   *
   * @param uuid field for assigning uuid to new object
   * @return AbstractObject with provided uuid
   */
  @Override
  public AbstractObject setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  /**
   * Returns new sensor object with the provided visibility
   *
   * @param visible boolean which indicates whether to make the object visible
   *                or invisible
   * @return OtherSensor with the provided visibility
   */
  @Override
  public OtherSensor setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}
