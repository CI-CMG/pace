package edu.colorado.cires.pace.data.translator;

import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class OrganizationTranslator extends Translator {
  private final String organizationUUID;
  private final String organizationName;
  private final String street;
  private final String city;
  private final String state;
  private final String zip;
  private final String country;
  private final String email;
  private final String phone;

  @Override
  public ObjectWithUniqueField setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }
}
