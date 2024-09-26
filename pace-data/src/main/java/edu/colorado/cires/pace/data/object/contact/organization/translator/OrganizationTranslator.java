package edu.colorado.cires.pace.data.object.contact.organization.translator;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.base.Translator;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * OrganizationTranslator holds onto the header fields for each organization field
 */
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
   * @return OrganizationTranslator object which has visibility dictated
   */
  @Override
  public OrganizationTranslator setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}
