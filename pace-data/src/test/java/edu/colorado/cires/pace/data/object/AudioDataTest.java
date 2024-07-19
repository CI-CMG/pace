package edu.colorado.cires.pace.data.object;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import org.junit.jupiter.api.Test;

public abstract class AudioDataTest<P extends AudioDataPackage> extends DataQualityTest<P> {
  
  @Test
  void testSetSensors() {
    List<String> sensors = List.of("1", "2");
    P p = createObject();
    assertNull(p.getSensors());
    p = (P) p.updateSensors(sensors);
    assertEquals(sensors, p.getSensors());
  }

}
