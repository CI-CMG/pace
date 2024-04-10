package edu.colorado.cires.pace.data.object;

import edu.colorado.cires.pace.data.validation.OrganizationValidator;
import edu.colorado.cires.pace.data.validation.ValidationException;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
public class Organization implements Contact {

  final UUID uuid;
  final String name;
  final String street;
  final String city;
  final String state;
  final String zip;
  final String country;
  final String email;
  final String phone;

  @Builder(toBuilder = true)
  @Jacksonized
  private Organization(UUID uuid, String name, String street, String city, String state, String zip, String country, String email, String phone)
      throws ValidationException {
    this.uuid = uuid;
    this.name = name;
    this.street = street;
    this.city = city;
    this.state = state;
    this.zip = zip;
    this.country = country;
    this.email = email;
    this.phone = phone;
    
    new OrganizationValidator().validate(this);
  }
}
