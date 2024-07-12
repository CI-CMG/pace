package edu.colorado.cires.pace.cli.command.translator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.translator.PersonTranslator;
import java.util.UUID;

class PersonTranslatorCommandTest extends TranslatorCommandTest<PersonTranslator> {

  @Override
  protected void assertTranslatorTypeSpecificFields(PersonTranslator expected, PersonTranslator actual) {
    assertEquals(expected.getPersonUUID(), actual.getPersonUUID());
    assertEquals(expected.getPersonName(), actual.getPersonName());
    assertEquals(expected.getOrganization(), actual.getOrganization());
    assertEquals(expected.getPosition(), actual.getPosition());
    assertEquals(expected.getStreet(), actual.getStreet());
    assertEquals(expected.getCity(), actual.getCity());
    assertEquals(expected.getState(), actual.getState());
    assertEquals(expected.getZip(), actual.getZip());
    assertEquals(expected.getCountry(), actual.getCountry());
    assertEquals(expected.getEmail(), actual.getEmail());
    assertEquals(expected.getPhone(), actual.getPhone());
    assertEquals(expected.getOrcid(), actual.getOrcid());
  }

  @Override
  public PersonTranslator createObject(String uniqueField, boolean withUUID) {
    return PersonTranslator.builder()
        .uuid(withUUID ? UUID.randomUUID() : null)
        .name(uniqueField)
        .personUUID("personUUID")
        .personName("personName")
        .organization("organization")
        .position("position")
        .street("street")
        .city("city")
        .state("state")
        .zip("zip")
        .country("country")
        .email("email")
        .phone("phone")
        .orcid("orcid")
        .build();
  }

  @Override
  protected PersonTranslator updateObject(PersonTranslator original, String uniqueField) {
    return original.toBuilder()
        .name(uniqueField)
        .build();
  }
}
