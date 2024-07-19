package edu.colorado.cires.pace.data.object;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MobileMarineLocationTest extends MarineLocationTest<MobileMarineLocation> {

  @Override
  protected MarineLocation createObject() {
    return MobileMarineLocation.builder().build();
  }
  
  @Test
  void testSetVessel() {
    MobileMarineLocation marineLocation = (MobileMarineLocation) createObject();
    assertNull(marineLocation.getVessel());
    marineLocation = marineLocation.setVessel("vessel");
    assertEquals("vessel", marineLocation.getVessel());
  }
}