package edu.colorado.cires.pace.cli.command.detectionType;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.cli.command.CommandTest;
import edu.colorado.cires.pace.data.object.DetectionType;
import java.util.List;

class DetectionTypeCommandTest extends CommandTest<DetectionType> {

  @Override
  public DetectionType createObject(String uniqueField) {
    return DetectionType.builder()
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
}