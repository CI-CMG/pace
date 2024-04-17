package edu.colorado.cires.pace.data.object;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class Person implements Contact {

  final UUID uuid;
  final String name;
  @NotBlank
  final String organization;
  @NotBlank
  final String position;
  final String street;
  final String city;
  final String state;
  final String zip;
  final String country;
  final String email;
  final String phone;
  final String orcid;

}
