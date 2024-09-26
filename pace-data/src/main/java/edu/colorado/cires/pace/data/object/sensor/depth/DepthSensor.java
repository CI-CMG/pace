package edu.colorado.cires.pace.data.object.sensor.depth;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.sensor.base.Sensor;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * DepthSensor extends Sensor
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class DepthSensor extends Sensor {

  /**
   * Returns new object with uuid set as provided
   *
   * @param uuid field for assigning uuid to new object
   * @return AbstractObject with provided uuid set
   */
  @Override
  public AbstractObject setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  /**
   * Returns sensor object with provided visibility
   *
   * @param visible boolean which indicates whether to make the object visible
   *                or invisible
   * @return DepthSensor with provided visibility
   */
  @Override
  public DepthSensor setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}
