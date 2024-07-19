package edu.colorado.cires.pace.data.object;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import edu.colorado.cires.pace.data.ObjectWithUniqueFieldTest;
import org.junit.jupiter.api.Test;

class FileTypeTest extends ObjectWithUniqueFieldTest<FileType> {

  @Override
  protected FileType createObject() {
    return FileType.builder().build();
  }
  
  @Test
  void testGetUniqueField() {
    FileType fileType = createObject();
    assertNull(fileType.getUniqueField());
    fileType = fileType.toBuilder().type("type").build();
    assertEquals("type", fileType.getUniqueField());
  }
}