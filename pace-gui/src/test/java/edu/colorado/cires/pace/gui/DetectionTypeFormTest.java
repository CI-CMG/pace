package edu.colorado.cires.pace.gui;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.cires.pace.data.object.detectionType.DetectionType;
import java.util.UUID;
import javax.swing.JPanel;

class DetectionTypeFormTest extends MetadataFormTest<DetectionType, DetectionTypeForm> {

  @Override
  protected DetectionTypeForm createMetadataForm(DetectionType initialObject) {
    return new DetectionTypeForm(initialObject);
  }

  @Override
  protected DetectionType createObject() {
    return DetectionType.builder()
        .uuid(UUID.randomUUID())
        .source("source")
        .scienceName("science-name")
        .visible(true)
        .build();
  }

  @Override
  protected void populateAdditionalFormFields(DetectionType object, JPanel contentPanel) {
    updateTextField(contentPanel, "scienceName", object.getScienceName());
  }

  @Override
  protected void assertAdditionalFieldsEqual(DetectionType expected, DetectionType actual) {
    assertEquals(expected.getScienceName(), actual.getScienceName());
  }
}