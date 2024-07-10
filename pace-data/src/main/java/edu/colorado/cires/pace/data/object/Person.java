package edu.colorado.cires.pace.data.object;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class Person implements Contact {

  final UUID uuid;
  @NotBlank
  final String name;
  @Size(min = 1, message = "must not be blank")
  final String organization;
  @Size(min = 1, message = "must not be blank")
  final String position;
  @Size(min = 1, message = "must not be blank")
  final String street;
  @Size(min = 1, message = "must not be blank")
  final String city;
  @Size(min = 1, message = "must not be blank")
  final String state;
  @Size(min = 1, message = "must not be blank")
  final String zip;
  @Size(min = 1, message = "must not be blank")
  final String country;
  @Size(min = 1, message = "must not be blank")
  final String email;
  @Size(min = 1, message = "must not be blank")
  final String phone;
  @Size(min = 1, message = "must not be blank")
  final String orcid;

}
