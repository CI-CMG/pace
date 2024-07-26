package edu.colorado.cires.pace.data.object.contact.person.translator;

import edu.colorado.cires.pace.data.object.base.ObjectWithUniqueField;
import edu.colorado.cires.pace.data.object.base.Translator;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class PersonTranslator extends Translator {
  private final String personUUID;
  private final String personName;
  private final String organization;
  private final String position;
  private final String street;
  private final String city;
  private final String state;
  private final String zip;
  private final String country;
  private final String email;
  private final String phone;
  private final String orcid;

  @Override
  public ObjectWithUniqueField setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  @Override
  public PersonTranslator setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}
