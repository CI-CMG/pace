package edu.colorado.cires.pace.cli.command.translator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.translator.FileTypeTranslator;

class FileTypeTranslatorCommandTest extends TranslatorCommandTest<FileTypeTranslator> {

  @Override
  protected void assertTranslatorTypeSpecificFields(FileTypeTranslator expected, FileTypeTranslator actual) {
    assertEquals(expected.getType(), actual.getType());
    assertEquals(expected.getComment(), actual.getComment());
    assertEquals(expected.getFileTypeUUID(), actual.getFileTypeUUID());
  }

  @Override
  public FileTypeTranslator createObject(String uniqueField) {
    return FileTypeTranslator.builder()
        .name(uniqueField)
        .type("type")
        .comment("comment")
        .fileTypeUUID("fileTypeUUID")
        .build();
  }

  @Override
  protected FileTypeTranslator updateObject(FileTypeTranslator original, String uniqueField) {
    return original.toBuilder()
        .name(uniqueField)
        .build();
  }
}
