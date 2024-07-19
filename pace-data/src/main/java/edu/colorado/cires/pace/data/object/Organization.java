package edu.colorado.cires.pace.data.object;

import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class Organization extends Contact {

  @Override
  public ObjectWithUniqueField setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }
}
