package edu.colorado.cires.pace.data.translator;

import edu.colorado.cires.pace.data.ObjectWithUniqueFieldTest;
import edu.colorado.cires.pace.data.object.platform.translator.PlatformTranslator;

class PlatformTranslatorTest extends ObjectWithUniqueFieldTest<PlatformTranslator> {

  @Override
  protected PlatformTranslator createObject() {
    return PlatformTranslator.builder().build();
  }
}