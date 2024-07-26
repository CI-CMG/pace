package edu.colorado.cires.pace.data.translator;

import edu.colorado.cires.pace.data.ObjectWithUniqueFieldTest;
import edu.colorado.cires.pace.data.object.dataset.base.translator.PackageTranslator;

class PackageTranslatorTest extends ObjectWithUniqueFieldTest<PackageTranslator> {

  @Override
  protected PackageTranslator createObject() {
    return PackageTranslator.builder().build();
  }
}