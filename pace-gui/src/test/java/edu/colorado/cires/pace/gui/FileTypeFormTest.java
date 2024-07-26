package edu.colorado.cires.pace.gui;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.cires.pace.data.object.fileType.FileType;
import java.util.UUID;
import javax.swing.JPanel;

class FileTypeFormTest extends MetadataFormTest<FileType, FileTypeForm> {

  @Override
  protected FileTypeForm createMetadataForm(FileType initialObject) {
    return new FileTypeForm(initialObject);
  }

  @Override
  protected FileType createObject() {
    return FileType.builder()
        .uuid(UUID.randomUUID())
        .visible(true)
        .type("file-type")
        .comment("comment")
        .build();
  }

  @Override
  protected void populateAdditionalFormFields(FileType object, JPanel contentPanel) {
    updateTextField(contentPanel, "comment", object.getComment());
  }

  @Override
  protected void assertAdditionalFieldsEqual(FileType expected, FileType actual) {
    assertEquals(expected.getComment(), actual.getComment());
  }
}