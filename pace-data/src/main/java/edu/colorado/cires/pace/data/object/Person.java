package edu.colorado.cires.pace.data.object;

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
public class Person extends Contact {
  @NotBlank
  final String organization;
  final String position;
  final String orcid;

  @Override
  public ObjectWithUniqueField setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }
}
