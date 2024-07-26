package edu.colorado.cires.pace.data.object.sensor.audio;

import edu.colorado.cires.pace.data.object.base.ObjectWithUniqueField;
import edu.colorado.cires.pace.data.object.sensor.base.Sensor;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class AudioSensor extends Sensor {
  @NotBlank
  private String hydrophoneId;
  @NotBlank
  private String preampId;

  @Override
  public ObjectWithUniqueField setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  @Override
  public AudioSensor setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}
