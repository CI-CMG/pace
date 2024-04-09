package edu.colorado.cires.pace.data;

import java.util.UUID;

public record Person(
    UUID uuid,
    String name,
    String organization,
    String position,
    String street,
    String city,
    String state,
    String zip,
    String country,
    String email,
    String phone,
    String orcid
) implements Contact {

  @Override
  public ObjectWithUUID copyWithNewUUID(UUID uuid) {
    return new Person(
        uuid,
        name,
        organization,
        position,
        street,
        city,
        state,
        zip,
        country,
        email,
        phone,
        orcid
    );
  }
}
