package edu.colorado.cires.pace.data.translator;

import edu.colorado.cires.pace.data.ObjectWithUniqueFieldTest;
import edu.colorado.cires.pace.data.object.detectionType.translator.DetectionTypeTranslator;

class DetectionTypeTranslatorTest extends ObjectWithUniqueFieldTest<DetectionTypeTranslator> {

  @Override
  protected DetectionTypeTranslator createObject() {
    return DetectionTypeTranslator.builder().build();
  }
}