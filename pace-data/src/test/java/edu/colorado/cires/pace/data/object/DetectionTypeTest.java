package edu.colorado.cires.pace.data.object;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import edu.colorado.cires.pace.data.ObjectWithUniqueFieldTest;
import edu.colorado.cires.pace.data.object.detectionType.DetectionType;
import org.junit.jupiter.api.Test;

class DetectionTypeTest extends ObjectWithUniqueFieldTest<DetectionType> {

  @Override
  protected DetectionType createObject() {
    return DetectionType.builder().build();
  }
  
  @Test
  void testGetUniqueField() {
    DetectionType detectionType = createObject();
    assertNull(detectionType.getUniqueField());
    detectionType = detectionType.toBuilder()
        .source("source")
        .build();
    assertEquals("source", detectionType.getUniqueField());
  }
}