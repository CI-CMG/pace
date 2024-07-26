package edu.colorado.cires.pace.data.object.sensor.other;

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
public class OtherSensor extends Sensor {
  @NotBlank
  private String sensorType;
  @NotBlank
  private String properties;

  @Override
  public ObjectWithUniqueField setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  @Override
  public OtherSensor setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}
