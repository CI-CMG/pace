package edu.colorado.cires.pace.data.object.contact.organization;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.contact.Contact;
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
  public AbstractObject setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  @Override
  public Organization setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}
