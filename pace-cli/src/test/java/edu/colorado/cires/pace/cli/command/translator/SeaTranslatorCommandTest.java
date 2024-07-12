package edu.colorado.cires.pace.cli.command.translator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.translator.SeaTranslator;
import java.util.UUID;

class SeaTranslatorCommandTest extends TranslatorCommandTest<SeaTranslator> {

  @Override
  protected void assertTranslatorTypeSpecificFields(SeaTranslator expected, SeaTranslator actual) {
    assertEquals(expected.getSeaUUID(), actual.getSeaUUID());
    assertEquals(expected.getSeaName(), actual.getSeaName());
  }

  @Override
  public SeaTranslator createObject(String uniqueField, boolean withUUID) {
    return SeaTranslator.builder()
        .uuid(withUUID ? UUID.randomUUID() : null)
        .name(uniqueField)
        .seaName("seaName")
        .seaUUID("seaUUID")
        .build();
  }

  @Override
  protected SeaTranslator updateObject(SeaTranslator original, String uniqueField) {
    return original.toBuilder()
        .name(uniqueField)
        .build();
  }
}
