package edu.colorado.cires.pace.data.translator;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class PersonTranslator implements Translator {

  private final UUID uuid;
  private final String name;

  private final String personUUID;
  private final String personName;
  private final String organization;
  private final String position;
  private final String street;
  private final String city;
  private final String state;
  private final String zip;
  private final String country;
  private final String email;
  private final String phone;
  private final String orcid;

}
