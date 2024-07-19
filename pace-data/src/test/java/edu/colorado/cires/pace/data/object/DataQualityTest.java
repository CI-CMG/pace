package edu.colorado.cires.pace.data.object;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public abstract class DataQualityTest<P extends Package> extends PackageTest<P> {
  
  @Test
  void testSetAnalyst() {
    DataQuality p = (DataQuality) createObject();
    assertNull(p.getQualityAnalyst());
    p = p.setQualityAnalyst("analyst");
    assertEquals("analyst", p.getQualityAnalyst());
  }

}
