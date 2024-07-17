package edu.colorado.cires.pace.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.data.object.DetectionType;
import java.util.List;
import org.assertj.swing.fixture.JPanelFixture;

class DetectionTypesPanelTest extends MetadataPanelTest<DetectionType> {

  @Override
  protected String getJsonFileName() {
    return "detection-types.json";
  }

  @Override
  protected String getMetadataTabTitle() {
    return "Detection Types";
  }

  @Override
  protected String getMetadataPanelName() {
    return "detectionTypesPanel";
  }

  @Override
  protected String getFormPanelName() {
    return "detectionTypeForm";
  }

  @Override
  protected void fillOutForm(JPanelFixture formFixture, DetectionType object) {
    enterFieldText(formFixture, "source", object.getSource());
    enterFieldText(formFixture, "scienceName", object.getScienceName());
  }

  @Override
  protected DetectionType createObject(String uniqueField) {
    return DetectionType.builder()
        .source(uniqueField)
        .scienceName("a")
        .build();
  }

  @Override
  protected String[] objectToRow(DetectionType object) {
    return new String[] {
        object.getSource(), object.getScienceName()
    };
  }

  @Override
  protected void requireFormContents(JPanelFixture formFixture, DetectionType object) {
    requireFieldText(formFixture, "source", object.getSource());
    requireFieldText(formFixture, "scienceName", object.getScienceName());
  }

  @Override
  protected DetectionType updateObject(DetectionType original, String uniqueField) {
    return original.toBuilder()
        .source(uniqueField)
        .scienceName("z")
        .build();
  }

  @Override
  protected String getUniqueField(DetectionType object) {
    return object.getSource();
  }

  @Override
  protected void assertObjectsEqual(DetectionType expected, DetectionType actual, boolean checkUUID) {
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    } else {
      assertNotNull(actual.getUuid());
    }
    
    assertEquals(expected.getSource(), actual.getSource());
    assertEquals(expected.getScienceName(), actual.getScienceName());
  }

  @Override
  protected TypeReference<List<DetectionType>> getTypeReference() {
    return new TypeReference<>() {};
  }

  @Override
  protected String getUniqueFieldName() {
    return "source";
  }

  @Override
  protected Class<DetectionType> getObjectClass() {
    return DetectionType.class;
  }

  @Override
  protected String[] getSpreadsheetHeaders() {
    return new String[] {
        "UUID", "Source", "Science Name"
    };
  }

  @Override
  protected void fillOutTranslatorForm(JPanelFixture panelFixture) {
    selectComboBoxOption(panelFixture, "translatorType", "Detection Type", 11);

    JPanelFixture typeSpecificForm = getPanel(panelFixture, "detectionTypeTranslatorForm");
    typeSpecificForm.comboBox("uuid")
        .requireVisible()
        .requireEnabled()
        .requireItemCount(3)
        .selectItem("UUID");
    typeSpecificForm.comboBox("source")
        .requireVisible()
        .requireEnabled()
        .requireItemCount(3)
        .selectItem("Source");
    typeSpecificForm.comboBox("scienceName")
        .requireVisible()
        .requireEnabled()
        .requireItemCount(3)
        .selectItem("Science Name");
  }
}
