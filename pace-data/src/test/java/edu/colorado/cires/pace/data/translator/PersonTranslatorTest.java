package edu.colorado.cires.pace.data.translator;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.cires.pace.data.ObjectWithUniqueFieldTest;

class PersonTranslatorTest extends ObjectWithUniqueFieldTest<PersonTranslator> {

  @Override
  protected PersonTranslator createObject() {
    return PersonTranslator.builder().build();
  }
}