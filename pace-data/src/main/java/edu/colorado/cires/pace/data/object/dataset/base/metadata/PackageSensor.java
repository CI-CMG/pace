package edu.colorado.cires.pace.data.object.dataset.base.metadata;

import edu.colorado.cires.pace.data.object.position.Position;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode
@Builder(toBuilder = true)
@Jacksonized
public class PackageSensor<T> {
  @NotNull @Valid
  private final T sensor;
  @NotNull @Valid
  private final Position position;
}
