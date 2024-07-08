package edu.colorado.cires.pace.cli.command.translator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.translator.OrganizationTranslator;

class OrganizationTranslatorCommandTest extends TranslatorCommandTest<OrganizationTranslator> {

  @Override
  protected void assertTranslatorTypeSpecificFields(OrganizationTranslator expected, OrganizationTranslator actual) {
    assertEquals(expected.getOrganizationUUID(), actual.getOrganizationUUID());
    assertEquals(expected.getOrganizationName(), actual.getOrganizationName());
    assertEquals(expected.getStreet(), actual.getStreet());
    assertEquals(expected.getCity(), actual.getCity());
    assertEquals(expected.getState(), actual.getState());
    assertEquals(expected.getZip(), actual.getZip());
    assertEquals(expected.getCountry(), actual.getCountry());
    assertEquals(expected.getEmail(), actual.getEmail());
    assertEquals(expected.getPhone(), actual.getPhone());
  }

  @Override
  public OrganizationTranslator createObject(String uniqueField) {
    return OrganizationTranslator.builder()
        .name(uniqueField)
        .organizationUUID("organizationUUID")
        .organizationName("organizationName")
        .street("street")
        .city("city")
        .state("state")
        .zip("zip")
        .country("country")
        .email("email")
        .phone("phone")
        .build();
  }

  @Override
  protected OrganizationTranslator updateObject(OrganizationTranslator original, String uniqueField) {
    return original.toBuilder()
        .name(uniqueField)
        .build();
  }
}
