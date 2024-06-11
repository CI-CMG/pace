package edu.colorado.cires.pace.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.translator.SeaTranslator;
import edu.colorado.cires.pace.data.translator.Translator;

public class SeaTranslatorRepositoryTest extends TranslatorRepositoryTest {

  @Override
  protected Translator createNewObject(int suffix) {
    return SeaTranslator.builder()
        .name(String.format("name-%s", suffix))
        .seaUUID(String.format("sea-uuid-%s", suffix))
        .seaName(String.format("sea-name-%s", suffix))
        .build();
  }

  @Override
  protected Translator copyWithUpdatedUniqueField(Translator object, String uniqueField) {
    return ((SeaTranslator) object).toBuilder()
        .name(uniqueField)
        .build();
  }

  @Override
  protected void assertObjectsEqual(Translator expected, Translator actual, boolean checkUUID) {
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    }
    assertEquals(expected.getName(), actual.getName());
    
    SeaTranslator expectedSeaTranslators = (SeaTranslator) expected;
    SeaTranslator actualSeaTranslators = (SeaTranslator) actual;
    assertEquals(expectedSeaTranslators.getSeaUUID(), actualSeaTranslators.getSeaUUID());
    assertEquals(expectedSeaTranslators.getSeaName(), actualSeaTranslators.getSeaName());
  }
}
