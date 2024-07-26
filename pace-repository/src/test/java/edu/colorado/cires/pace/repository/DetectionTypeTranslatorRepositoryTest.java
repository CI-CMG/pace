package edu.colorado.cires.pace.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.object.detectionType.translator.DetectionTypeTranslator;
import edu.colorado.cires.pace.data.object.base.Translator;

public class DetectionTypeTranslatorRepositoryTest extends TranslatorRepositoryTest {

  @Override
  protected Translator createNewObject(int suffix) {
    return DetectionTypeTranslator.builder()
        .name(String.format("name-%s", suffix))
        .detectionTypeUUID(String.format("uuid-%s", suffix))
        .source(String.format("source-%s", suffix))
        .scienceName(String.format("science-name-%s", suffix))
        .build();
  }

  @Override
  protected Translator copyWithUpdatedUniqueField(Translator object, String uniqueField) {
    return ((DetectionTypeTranslator) object).toBuilder()
        .name(uniqueField)
        .build();
  }

  @Override
  protected void assertObjectsEqual(Translator expected, Translator actual, boolean checkUUID) {
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    }
    assertEquals(expected.getName(), actual.getName());
    
    DetectionTypeTranslator expectedDetectionTypeTranslator = (DetectionTypeTranslator) expected;
    DetectionTypeTranslator actualDetectionTypeTranslator = (DetectionTypeTranslator) actual;
    assertEquals(expectedDetectionTypeTranslator.getDetectionTypeUUID(), actualDetectionTypeTranslator.getDetectionTypeUUID());
    assertEquals(expectedDetectionTypeTranslator.getSource(), actualDetectionTypeTranslator.getSource());
    assertEquals(expectedDetectionTypeTranslator.getScienceName(), actualDetectionTypeTranslator.getScienceName());
  }
}
