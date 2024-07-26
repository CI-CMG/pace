package edu.colorado.cires.pace.data.translator;

import edu.colorado.cires.pace.data.ObjectWithUniqueFieldTest;
import edu.colorado.cires.pace.data.object.contact.organization.translator.OrganizationTranslator;

class OrganizationTranslatorTest extends ObjectWithUniqueFieldTest<OrganizationTranslator> {

  @Override
  protected OrganizationTranslator createObject() {
    return OrganizationTranslator.builder().build();
  }
}