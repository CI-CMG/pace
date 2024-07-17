package edu.colorado.cires.pace.data.object;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

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
