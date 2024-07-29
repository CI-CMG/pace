package edu.colorado.cires.pace.data.object.contact.person;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.contact.Contact;
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
  public AbstractObject setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  @Override
  public Person setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}
