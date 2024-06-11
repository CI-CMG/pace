package edu.colorado.cires.pace.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.translator.PersonTranslator;
import edu.colorado.cires.pace.data.translator.Translator;

public class PersonTranslatorRepositoryTest extends TranslatorRepositoryTest {

  @Override
  protected Translator createNewObject(int suffix) {
    return PersonTranslator.builder()
        .name(String.format("name-%s", suffix))
        .personUUID(String.format("uuid-%s", suffix))
        .personName(String.format("person-name-%s", suffix))
        .organization(String.format("organization-%s", suffix))
        .position(String.format("position-%s", suffix))
        .street(String.format("street-%s", suffix))
        .city(String.format("city-%s", suffix))
        .state(String.format("state-%s", suffix))
        .zip(String.format("zip-%s", suffix))
        .country(String.format("country-%s", suffix))
        .email(String.format("email-%s", suffix))
        .phone(String.format("phone-%s", suffix))
        .orcid(String.format("orcid-%s", suffix))
        .build();
  }

  @Override
  protected Translator copyWithUpdatedUniqueField(Translator object, String uniqueField) {
    return ((PersonTranslator) object).toBuilder()
        .name(uniqueField)
        .build();
  }

  @Override
  protected void assertObjectsEqual(Translator expected, Translator actual, boolean checkUUID) {
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    }
    assertEquals(expected.getName(), actual.getName());
    
    PersonTranslator expectedPersonTranslator = (PersonTranslator) expected;
    PersonTranslator actualPersonTranslator = (PersonTranslator) actual;
    assertEquals(expectedPersonTranslator.getPersonUUID(), actualPersonTranslator.getPersonUUID());
    assertEquals(expectedPersonTranslator.getPersonName(), actualPersonTranslator.getPersonName());
    assertEquals(expectedPersonTranslator.getOrganization(), actualPersonTranslator.getOrganization());
    assertEquals(expectedPersonTranslator.getPosition(), actualPersonTranslator.getPosition());
    assertEquals(expectedPersonTranslator.getStreet(), actualPersonTranslator.getStreet());
    assertEquals(expectedPersonTranslator.getCity(), actualPersonTranslator.getCity());
    assertEquals(expectedPersonTranslator.getState(), actualPersonTranslator.getState());
    assertEquals(expectedPersonTranslator.getZip(), actualPersonTranslator.getZip());
    assertEquals(expectedPersonTranslator.getCountry(), actualPersonTranslator.getCountry());
    assertEquals(expectedPersonTranslator.getEmail(), actualPersonTranslator.getEmail());
    assertEquals(expectedPersonTranslator.getPhone(), actualPersonTranslator.getPhone());
    assertEquals(expectedPersonTranslator.getOrcid(), actualPersonTranslator.getOrcid());
  }
}
