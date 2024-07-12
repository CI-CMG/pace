package edu.colorado.cires.pace.cli.command.detectionType;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.cli.command.TranslateCommandTest;
import edu.colorado.cires.pace.data.object.DetectionType;
import edu.colorado.cires.pace.data.translator.DetectionTypeTranslator;
import java.util.List;
import java.util.UUID;

class DetectionTypeCommandTest extends TranslateCommandTest<DetectionType, DetectionTypeTranslator> {

  @Override
  public DetectionType createObject(String uniqueField, boolean withUUID) {
    return DetectionType.builder()
        .uuid(withUUID ? UUID.randomUUID() : null)
        .source(uniqueField)
        .scienceName("science-name")
        .build();
  }

  @Override
  protected String getRepositoryFileName() {
    return "detection-types.json";
  }

  @Override
  protected String getCommandPrefix() {
    return "detection-type";
  }

  @Override
  protected TypeReference<List<DetectionType>> getTypeReference() {
    return new TypeReference<>() {};
  }

  @Override
  protected Class<DetectionType> getClazz() {
    return DetectionType.class;
  }

  @Override
  protected String getUniqueFieldName() {
    return "source";
  }

  @Override
  protected void assertObjectsEqual(DetectionType expected, DetectionType actual, boolean checkUUID) {
    assertEquals(expected.getSource(), actual.getSource());
    assertEquals(expected.getScienceName(), actual.getScienceName());
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    } else {
      assertNotNull(actual.getUuid());
    }
  }

  @Override
  protected String getUniqueField(DetectionType object) {
    return object.getSource();
  }

  @Override
  protected DetectionType updateObject(DetectionType original, String uniqueField) {
    return original.toBuilder()
        .source(uniqueField)
        .build();
  }

  @Override
  protected String[] getTranslatorFields() {
    return new String[] {
        "UUID",
        "source",
        "scienceName"
    };
  }

  @Override
  protected DetectionTypeTranslator createTranslator(String name) {
    return DetectionTypeTranslator.builder()
        .name(name)
        .detectionTypeUUID("UUID")
        .source("source")
        .scienceName("scienceName")
        .build();
  }

  @Override
  protected String[] objectToRow(DetectionType object) {
    return new String[] {
        object.getUuid() == null ? "" : object.getUuid().toString(),
        object.getSource(),
        object.getScienceName()
    };
  }
}