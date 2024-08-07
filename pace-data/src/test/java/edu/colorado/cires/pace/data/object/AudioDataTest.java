package edu.colorado.cires.pace.data.object;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import edu.colorado.cires.pace.data.object.dataset.audio.AudioDataPackage;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.PackageSensor;
import edu.colorado.cires.pace.data.object.position.Position;
import java.util.List;
import org.junit.jupiter.api.Test;

public abstract class AudioDataTest<P extends AudioDataPackage> extends DataQualityTest<P> {
  
  @Test
  void testSetSensors() {
    List<PackageSensor<String>> sensors = List.of(
        PackageSensor.<String>builder()
            .sensor("1")
            .position(Position.builder()
                .x(1f)
                .y(2f)
                .z(3f)
                .build())
            .build(),
        PackageSensor.<String>builder()
            .sensor("2")
            .position(Position.builder()
                .x(4f)
                .y(5f)
                .z(6f)
                .build())
            .build()
    );
    P p = createObject();
    assertEquals(0, p.getSensors().size());
    p = (P) p.updateSensors(sensors);
    List<PackageSensor<String>> actualSensors = p.getSensors();
    assertEquals(sensors.size(), actualSensors.size());

    for (int i = 0; i < sensors.size(); i++) {
      PackageSensor<String> expectedSensor = sensors.get(i);
      PackageSensor<String> actualSensor = actualSensors.get(i);
      assertEquals(expectedSensor, actualSensor);
    }
  }

}
