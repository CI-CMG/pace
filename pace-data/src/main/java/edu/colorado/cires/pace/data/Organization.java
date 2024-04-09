package edu.colorado.cires.pace.data;

import java.util.UUID;

public record Organization(
    UUID uuid,
    String name,
    String street,
    String city,
    String state,
    String zip,
    String country,
    String email,
    String phone
) implements Contact {

  @Override
  public ObjectWithUUID copyWithNewUUID(UUID uuid) {
    return new Organization(
        uuid,
        name,
        street,
        city,
        state,
        zip,
        country,
        email,
        phone
    );
  }
}
