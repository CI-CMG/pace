package edu.colorado.cires.pace.data.object;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public abstract class MarineLocationTest<L extends MarineLocation> {
  
  protected abstract MarineLocation createObject();
  
  @Test
  void testSetSeaArea() {
    MarineLocation marineLocation = createObject();
    assertNull(marineLocation.getSeaArea());
    marineLocation = marineLocation.setSeaArea("sea-area");
    assertEquals("sea-area", marineLocation.getSeaArea());
  }

}