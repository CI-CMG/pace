package edu.colorado.cires.pace.cli.command.translator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.translator.PlatformTranslator;

class PlatformTranslatorCommandTest extends TranslatorCommandTest<PlatformTranslator> {

  @Override
  protected void assertTranslatorTypeSpecificFields(PlatformTranslator expected, PlatformTranslator actual) {
    assertEquals(expected.getPlatformUUID(), actual.getPlatformUUID());
    assertEquals(expected.getPlatformName(), actual.getPlatformName());
  }

  @Override
  public PlatformTranslator createObject(String uniqueField) {
    return PlatformTranslator.builder()
        .name(uniqueField)
        .platformUUID("platformUUID")
        .platformName("platformName")
        .build();
  }

  @Override
  protected PlatformTranslator updateObject(PlatformTranslator original, String uniqueField) {
    return original.toBuilder()
        .name(uniqueField)
        .build();
  }
}
