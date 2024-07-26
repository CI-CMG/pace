package edu.colorado.cires.pace.data.translator;

import edu.colorado.cires.pace.data.ObjectWithUniqueFieldTest;
import edu.colorado.cires.pace.data.object.contact.person.translator.PersonTranslator;

class PersonTranslatorTest extends ObjectWithUniqueFieldTest<PersonTranslator> {

  @Override
  protected PersonTranslator createObject() {
    return PersonTranslator.builder().build();
  }
}