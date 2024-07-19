package edu.colorado.cires.pace.data.translator;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.cires.pace.data.ObjectWithUniqueFieldTest;

class OrganizationTranslatorTest extends ObjectWithUniqueFieldTest<OrganizationTranslator> {

  @Override
  protected OrganizationTranslator createObject() {
    return OrganizationTranslator.builder().build();
  }
}