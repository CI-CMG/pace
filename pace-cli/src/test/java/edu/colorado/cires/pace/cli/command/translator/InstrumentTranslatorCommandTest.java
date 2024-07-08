package edu.colorado.cires.pace.cli.command.translator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.translator.InstrumentTranslator;

class InstrumentTranslatorCommandTest extends TranslatorCommandTest<InstrumentTranslator> {

  @Override
  protected void assertTranslatorTypeSpecificFields(InstrumentTranslator expected, InstrumentTranslator actual) {
    assertEquals(expected.getInstrumentUUID(), actual.getInstrumentUUID());
    assertEquals(expected.getInstrumentName(), actual.getInstrumentName());
    assertEquals(expected.getFileTypes(), actual.getFileTypes());
  }

  @Override
  public InstrumentTranslator createObject(String uniqueField) {
    return InstrumentTranslator.builder()
        .name(uniqueField)
        .instrumentUUID("instrumentUUID")
        .instrumentName("instrumentName")
        .fileTypes("fileTypes")
        .build();
  }

  @Override
  protected InstrumentTranslator updateObject(InstrumentTranslator original, String uniqueField) {
    return original.toBuilder()
        .name(uniqueField)
        .build();
  }
}
