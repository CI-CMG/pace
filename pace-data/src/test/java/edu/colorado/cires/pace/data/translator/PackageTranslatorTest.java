package edu.colorado.cires.pace.data.translator;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.cires.pace.data.ObjectWithUniqueFieldTest;

class PackageTranslatorTest extends ObjectWithUniqueFieldTest<PackageTranslator> {

  @Override
  protected PackageTranslator createObject() {
    return PackageTranslator.builder().build();
  }
}