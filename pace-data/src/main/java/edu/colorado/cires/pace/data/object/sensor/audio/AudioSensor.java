package edu.colorado.cires.pace.data.object.sensor.audio;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.sensor.base.Sensor;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * AudioSensor extends Sensor and hold hydrophoneId and preampId
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class AudioSensor extends Sensor {
  @NotBlank
  private String hydrophoneId;
  @NotBlank
  private String preampId;

  /**
   * Returns a new object with the provided uuid set
   *
   * @param uuid field for assigning uuid to new object
   * @return AbstractObject with provided uuid
   */
  @Override
  public AbstractObject setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  /**
   * Returns an audio sensor object with the provided visibility set
   *
   * @param visible boolean which indicates whether to make the object visible
   *                or invisible
   * @return AudioSensor with provided visibility
   */
  @Override
  public AudioSensor setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}
