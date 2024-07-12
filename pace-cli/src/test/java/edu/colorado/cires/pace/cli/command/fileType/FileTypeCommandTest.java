package edu.colorado.cires.pace.cli.command.fileType;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.cli.command.TranslateCommandTest;
import edu.colorado.cires.pace.data.object.FileType;
import edu.colorado.cires.pace.data.translator.FileTypeTranslator;
import java.util.List;
import java.util.UUID;

public class FileTypeCommandTest extends TranslateCommandTest<FileType, FileTypeTranslator> {

  @Override
  public FileType createObject(String uniqueField, boolean withUUID) {
    return FileType.builder()
        .uuid(withUUID ? UUID.randomUUID() : null)
        .type(uniqueField)
        .comment("comment")
        .build();
  }

  @Override
  protected String getRepositoryFileName() {
    return "file-types.json";
  }

  @Override
  protected String getCommandPrefix() {
    return "file-type";
  }

  @Override
  protected TypeReference<List<FileType>> getTypeReference() {
    return new TypeReference<>() {};
  }

  @Override
  protected Class<FileType> getClazz() {
    return FileType.class;
  }

  @Override
  protected String getUniqueFieldName() {
    return "type";
  }

  @Override
  protected void assertObjectsEqual(FileType expected, FileType actual, boolean checkUUID) {
    assertFileTypesEqual(expected, actual, checkUUID);
  }
  
  public static void assertFileTypesEqual(FileType expected, FileType actual, boolean checkUUID) {
    assertEquals(expected.getType(), actual.getType());
    assertEquals(expected.getComment(), actual.getComment());
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    } else {
      assertNotNull(actual.getUuid());
    }
  }

  @Override
  protected String getUniqueField(FileType object) {
    return object.getType();
  }

  @Override
  protected FileType updateObject(FileType original, String uniqueField) {
    return original.toBuilder()
        .type(uniqueField)
        .build();
  }

  @Override
  protected String[] getTranslatorFields() {
    return new String[] {
        "UUID",
        "type",
        "comment"
    };
  }

  @Override
  protected FileTypeTranslator createTranslator(String name) {
    return FileTypeTranslator.builder()
        .name(name)
        .fileTypeUUID("UUID")
        .type("type")
        .comment("comment")
        .build();
  }

  @Override
  protected String[] objectToRow(FileType object) {
    return new String[] {
        object.getUuid() == null ? "" : object.getUuid().toString(),
        object.getType(),
        object.getComment()
    };
  }
}