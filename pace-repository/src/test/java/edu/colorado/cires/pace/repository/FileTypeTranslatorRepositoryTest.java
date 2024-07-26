package edu.colorado.cires.pace.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.object.fileType.translator.FileTypeTranslator;
import edu.colorado.cires.pace.data.object.base.Translator;

public class FileTypeTranslatorRepositoryTest extends TranslatorRepositoryTest {

  @Override
  protected String getUniqueFieldName() {
    return "name";
  }

  @Override
  protected Translator createNewObject(int suffix) {
    return FileTypeTranslator.builder()
        .name(String.format("name-%s", suffix))
        .fileTypeUUID(String.format("uuid-%s", suffix))
        .type(String.format("type-%s", suffix))
        .comment(String.format("comment-%s", suffix))
        .build();
  }

  @Override
  protected Translator copyWithUpdatedUniqueField(Translator object, String uniqueField) {
    return ((FileTypeTranslator) object).toBuilder()
        .name(uniqueField)
        .build();
  }

  @Override
  protected void assertObjectsEqual(Translator expected, Translator actual, boolean checkUUID) {
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    }
    assertEquals(expected.getName(), actual.getName());
    
    FileTypeTranslator expectedFileTypeTranslator = (FileTypeTranslator) expected;
    FileTypeTranslator actualFileTypeTranslator = (FileTypeTranslator) actual;
    assertEquals(expectedFileTypeTranslator.getFileTypeUUID(), actualFileTypeTranslator.getFileTypeUUID());
    assertEquals(expectedFileTypeTranslator.getType(), actualFileTypeTranslator.getType());
    assertEquals(expectedFileTypeTranslator.getComment(), actualFileTypeTranslator.getComment());
  }
}
