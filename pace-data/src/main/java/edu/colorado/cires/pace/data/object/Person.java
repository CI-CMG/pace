package edu.colorado.cires.pace.data.object;

import edu.colorado.cires.pace.data.validation.PersonValidator;
import edu.colorado.cires.pace.data.validation.ValidationException;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
public class Person implements Contact {

  final UUID uuid;
  final String name;
  final String organization;
  final String position;
  final String street;
  final String city;
  final String state;
  final String zip;
  final String country;
  final String email;
  final String phone;
  final String orcid;

  @Builder(toBuilder = true)
  @Jacksonized
  private Person(UUID uuid, String name, String organization, String position, String street, String city, String state, String zip, String country,
      String email, String phone, String orcid) throws ValidationException {
    this.uuid = uuid;
    this.name = name;
    this.organization = organization;
    this.position = position;
    this.street = street;
    this.city = city;
    this.state = state;
    this.zip = zip;
    this.country = country;
    this.email = email;
    this.phone = phone;
    this.orcid = orcid;
    
    new PersonValidator().validate(this);
  }
}
