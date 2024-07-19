package edu.colorado.cires.pace.data.translator;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.cires.pace.data.ObjectWithUniqueFieldTest;

class DetectionTypeTranslatorTest extends ObjectWithUniqueFieldTest<DetectionTypeTranslator> {

  @Override
  protected DetectionTypeTranslator createObject() {
    return DetectionTypeTranslator.builder().build();
  }
}