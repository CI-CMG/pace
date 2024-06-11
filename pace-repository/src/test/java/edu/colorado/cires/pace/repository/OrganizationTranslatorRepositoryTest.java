package edu.colorado.cires.pace.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.object.Organization;
import edu.colorado.cires.pace.data.translator.OrganizationTranslator;
import edu.colorado.cires.pace.data.translator.Translator;

public class OrganizationTranslatorRepositoryTest extends TranslatorRepositoryTest {

  @Override
  protected Translator createNewObject(int suffix) {
    return OrganizationTranslator.builder()
        .name(String.format("name-%s", suffix))
        .organizationUUID(String.format("uuid-%s", suffix))
        .organizationName(String.format("org-name-%s", suffix))
        .street(String.format("street-%s", suffix))
        .city(String.format("city-%s", suffix))
        .state(String.format("state-%s", suffix))
        .zip(String.format("zip-%s", suffix))
        .country(String.format("country-%s", suffix))
        .email(String.format("email-%s", suffix))
        .phone(String.format("phone-%s", suffix))
        .build();
  }

  @Override
  protected Translator copyWithUpdatedUniqueField(Translator object, String uniqueField) {
    return ((OrganizationTranslator) object).toBuilder()
        .name(uniqueField)
        .build();
  }

  @Override
  protected void assertObjectsEqual(Translator expected, Translator actual, boolean checkUUID) {
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    }
    assertEquals(expected.getName(), actual.getName());
    
    OrganizationTranslator expectedOrganizationTranslator = (OrganizationTranslator) expected;
    OrganizationTranslator actualOrganizationTranslator = (OrganizationTranslator) actual;
    assertEquals(expectedOrganizationTranslator.getOrganizationUUID(), actualOrganizationTranslator.getOrganizationUUID());
    assertEquals(expectedOrganizationTranslator.getOrganizationName(), actualOrganizationTranslator.getOrganizationName());
    assertEquals(expectedOrganizationTranslator.getStreet(), actualOrganizationTranslator.getStreet());
    assertEquals(expectedOrganizationTranslator.getCity(), actualOrganizationTranslator.getCity());
    assertEquals(expectedOrganizationTranslator.getState(), actualOrganizationTranslator.getState());
    assertEquals(expectedOrganizationTranslator.getZip(), actualOrganizationTranslator.getZip());
    assertEquals(expectedOrganizationTranslator.getCountry(), actualOrganizationTranslator.getCountry());
    assertEquals(expectedOrganizationTranslator.getEmail(), actualOrganizationTranslator.getEmail());
    assertEquals(expectedOrganizationTranslator.getPhone(), actualOrganizationTranslator.getPhone());
  }
}
