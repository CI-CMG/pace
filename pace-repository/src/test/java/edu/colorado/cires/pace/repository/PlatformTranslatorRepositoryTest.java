package edu.colorado.cires.pace.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.translator.PlatformTranslator;
import edu.colorado.cires.pace.data.translator.Translator;

public class PlatformTranslatorRepositoryTest extends TranslatorRepositoryTest {

  @Override
  protected Translator createNewObject(int suffix) {
    return PlatformTranslator.builder()
        .name(String.format("name-%s", suffix))
        .platformUUID(String.format("platform-uuid-%s", suffix))
        .platformName(String.format("platform-name-%s", suffix))
        .build();
  }

  @Override
  protected Translator copyWithUpdatedUniqueField(Translator object, String uniqueField) {
    return ((PlatformTranslator) object).toBuilder()
        .name(uniqueField)
        .build();
  }

  @Override
  protected void assertObjectsEqual(Translator expected, Translator actual, boolean checkUUID) {
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    }
    assertEquals(expected.getName(), actual.getName());
    
    PlatformTranslator expectedPlatformTranslator = (PlatformTranslator) expected;
    PlatformTranslator actualPlatformTranslator = (PlatformTranslator) actual;
    assertEquals(expectedPlatformTranslator.getPlatformUUID(), actualPlatformTranslator.getPlatformUUID());
    assertEquals(expectedPlatformTranslator.getPlatformName(), actualPlatformTranslator.getPlatformName());
  }
}
