package edu.colorado.cires.pace.data.object.contact.organization;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.contact.Contact;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Organization objects contain all the fields of a regular contact and
 * represent an organizations in pace
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class Organization extends Contact {

  /**
   * Creates an object which is identical besides the newly set uuid
   *
   * @param uuid field for assigning uuid to new object
   * @return new abstract object with the provided uuid
   */
  @Override
  public AbstractObject setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  /**
   * Creates an object which is identical besides the newly set visibility
   *
   * @param visible boolean which indicates whether to make the object visible
   *                or invisible
   * @return Organization object which has visibility dictated
   */
  @Override
  public Organization setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}
