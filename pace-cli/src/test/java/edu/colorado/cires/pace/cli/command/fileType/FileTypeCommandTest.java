package edu.colorado.cires.pace.cli.command.fileType;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.cli.command.CommandTest;
import edu.colorado.cires.pace.data.object.FileType;
import java.util.List;

public class FileTypeCommandTest extends CommandTest<FileType> {

  @Override
  public FileType createObject(String uniqueField) {
    return FileType.builder()
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

}