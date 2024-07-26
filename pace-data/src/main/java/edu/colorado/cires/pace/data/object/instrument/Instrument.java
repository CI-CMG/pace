package edu.colorado.cires.pace.data.object.instrument;

import edu.colorado.cires.pace.data.object.base.ObjectWithName;
import edu.colorado.cires.pace.data.object.base.ObjectWithUniqueField;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class Instrument extends ObjectWithName {
  
  @NotEmpty @NotNull
  private final List<@NotBlank String> fileTypes;

  @Override
  public ObjectWithUniqueField setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  @Override
  public Instrument setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}