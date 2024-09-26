package edu.colorado.cires.pace.data.object.contact.person;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.contact.Contact;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Person objects contain all the fields of a regular contact as well as
 * organization, position, and orcid and represent a person in pace
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class Person extends Contact {
  @NotBlank
  final String organization;
  final String position;
  final String orcid;

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
   * @return Person object which has visibility dictated
   */
  @Override
  public Person setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}
