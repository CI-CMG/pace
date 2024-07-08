package edu.colorado.cires.pace.cli.command.translator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.translator.DetectionTypeTranslator;

class DetectionTypeTranslatorCommandTest extends TranslatorCommandTest<DetectionTypeTranslator> {

  @Override
  protected void assertTranslatorTypeSpecificFields(DetectionTypeTranslator expected, DetectionTypeTranslator actual) {
    assertEquals(expected.getSource(), actual.getSource());
    assertEquals(expected.getScienceName(), actual.getScienceName());
    assertEquals(expected.getDetectionTypeUUID(), actual.getDetectionTypeUUID());
  }

  @Override
  public DetectionTypeTranslator createObject(String uniqueField) {
    return DetectionTypeTranslator.builder()
        .name(uniqueField)
        .scienceName("scienceName")
        .source("source")
        .detectionTypeUUID("detectionTypeUUID")
        .build();
  }

  @Override
  protected DetectionTypeTranslator updateObject(DetectionTypeTranslator original, String uniqueField) {
    return original.toBuilder()
        .name(uniqueField)
        .build();
  }
}
