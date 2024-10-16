package edu.colorado.cires.pace.data.object.contact;

import edu.colorado.cires.pace.data.object.base.ObjectWithName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * Contact defines the standard fields for people and organizations
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public abstract class Contact extends ObjectWithName {
  private final String street;
  private final String city;
  private final String state;
  private final String zip;
  private final String country;
  private final String email;
  private final String phone;

}
