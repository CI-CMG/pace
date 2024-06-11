package edu.colorado.cires.pace.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.translator.InstrumentTranslator;
import edu.colorado.cires.pace.data.translator.Translator;

public class InstrumentTranslatorRepositoryTest extends TranslatorRepositoryTest {

  @Override
  protected Translator createNewObject(int suffix) {
    return InstrumentTranslator.builder()
        .name(String.format("name-%s", suffix))
        .instrumentUUID(String.format("uuid-%s", suffix))
        .instrumentName(String.format("instrument-name-%s", suffix))
        .fileTypes(String.format("fileTypes-%s", suffix))
        .build();
  }

  @Override
  protected Translator copyWithUpdatedUniqueField(Translator object, String uniqueField) {
    return ((InstrumentTranslator) object).toBuilder()
        .name(uniqueField)
        .build();
  }

  @Override
  protected void assertObjectsEqual(Translator expected, Translator actual, boolean checkUUID) {
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    }
    assertEquals(expected.getName(), actual.getName());
    
    InstrumentTranslator expectedInstrumentTranslator = (InstrumentTranslator) expected;
    InstrumentTranslator actualInstrumentTranslator = (InstrumentTranslator) actual;
    assertEquals(expectedInstrumentTranslator.getInstrumentUUID(), actualInstrumentTranslator.getInstrumentUUID());
    assertEquals(expectedInstrumentTranslator.getInstrumentName(), actualInstrumentTranslator.getInstrumentName());
    assertEquals(expectedInstrumentTranslator.getFileTypes(), actualInstrumentTranslator.getFileTypes());
  }
}
