package edu.colorado.cires.pace.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.data.object.FileType;
import java.util.List;
import org.assertj.swing.fixture.JPanelFixture;

class FileTypesPanelTest extends MetadataPanelTest<FileType> {

  @Override
  protected String getJsonFileName() {
    return "file-types.json";
  }

  @Override
  protected String getMetadataTabTitle() {
    return "File Types";
  }

  @Override
  protected String getMetadataPanelName() {
    return "fileTypesPanel";
  }

  @Override
  protected String getFormPanelName() {
    return "fileTypeForm";
  }

  @Override
  protected void fillOutForm(JPanelFixture formFixture, FileType object) {
    enterFieldText(formFixture, "type", object.getType());
    enterFieldText(formFixture, "comment", object.getComment());
  }

  @Override
  protected FileType createObject(String uniqueField) {
    return FileType.builder()
        .type(uniqueField)
        .comment("a")
        .build();
  }

  @Override
  protected String[] objectToRow(FileType object) {
    return new String[] {
        object.getType(), object.getComment()
    };
  }

  @Override
  protected void requireFormContents(JPanelFixture formFixture, FileType object) {
    requireFieldText(formFixture, "uuid", object.getUuid().toString());
    requireFieldText(formFixture, "type", object.getType());
    requireFieldText(formFixture, "comment", object.getComment());
  }

  @Override
  protected FileType updateObject(FileType original, String uniqueField) {
    return original.toBuilder()
        .type(uniqueField)
        .comment("b")
        .build();
  }

  @Override
  protected String getUniqueField(FileType object) {
    return object.getType();
  }

  @Override
  protected void assertObjectsEqual(FileType expected, FileType actual, boolean checkUUID) {
    assertFileTypesEquals(expected, actual, checkUUID);
  }
  
  public static void assertFileTypesEquals(FileType expected, FileType actual, boolean checkUUID) {
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    } else {
      assertNotNull(actual.getUuid());
    }

    assertEquals(expected.getType(), actual.getType());
    assertEquals(expected.getComment(), actual.getComment());
  }

  @Override
  protected TypeReference<List<FileType>> getTypeReference() {
    return new TypeReference<>() {};
  }

  @Override
  protected String getUniqueFieldName() {
    return "type";
  }

  @Override
  protected Class<FileType> getObjectClass() {
    return FileType.class;
  }

  @Override
  protected String[] getSpreadsheetHeaders() {
    return new String[] {
        "UUID", "Type", "Comment"
    };
  }

  @Override
  protected void fillOutTranslatorForm(JPanelFixture panelFixture) {
    selectComboBoxOption(panelFixture, "translatorType", "File Type", 11);
    
    JPanelFixture formFixture = getPanel(panelFixture, "fileTypeTranslatorForm");
    selectComboBoxOption(formFixture, "uuid", "UUID", 3);
    selectComboBoxOption(formFixture, "type", "Type", 3);
    selectComboBoxOption(formFixture, "comment", "Comment", 3);
  }
}
