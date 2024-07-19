package edu.colorado.cires.pace.data.translator;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.cires.pace.data.ObjectWithUniqueFieldTest;

class PlatformTranslatorTest extends ObjectWithUniqueFieldTest<PlatformTranslator> {

  @Override
  protected PlatformTranslator createObject() {
    return PlatformTranslator.builder().build();
  }
}