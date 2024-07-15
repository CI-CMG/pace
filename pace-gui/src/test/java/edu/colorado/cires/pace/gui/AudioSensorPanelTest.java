package edu.colorado.cires.pace.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import edu.colorado.cires.pace.data.object.AudioSensor;
import edu.colorado.cires.pace.data.object.Position;
import org.assertj.swing.fixture.JPanelFixture;

class AudioSensorPanelTest extends SensorsPanelTest<AudioSensor> {

  @Override
  protected void fillOutForm(JPanelFixture formFixture, AudioSensor object) {
    if (object.getUuid() == null) {
      formFixture.comboBox()
          .requireVisible()
          .requireEnabled()
          .requireItemCount(3)
          .selectItem("audio");
    }
    
    enterFieldText(formFixture, "name", object.getName());
    enterFieldText(formFixture, "description", object.getDescription());
    enterFieldText(formFixture, "hydrophoneId", object.getHydrophoneId());
    enterFieldText(formFixture, "preampId", object.getPreampId());
    Position position = object.getPosition();
    enterFieldText(formFixture, "x", position.getX().toString());
    enterFieldText(formFixture, "y", position.getY().toString());
    enterFieldText(formFixture, "z", position.getZ().toString());
  }

  @Override
  protected AudioSensor createObject(String uniqueField) {
    return AudioSensor.builder()
        .name(uniqueField)
        .description("a")
        .position(Position.builder()
            .x(1f)
            .y(2f)
            .z(3f)
            .build())
        .hydrophoneId("b")
        .preampId("c")
        .build();
  }

  @Override
  protected void requireFormContents(JPanelFixture formFixture, AudioSensor object) {
    requireFieldText(formFixture, "name", object.getName());
    requireFieldText(formFixture, "description", object.getDescription());
    requireFieldText(formFixture, "hydrophoneId", object.getHydrophoneId());
    requireFieldText(formFixture, "preampId", object.getPreampId());
    Position position = object.getPosition();
    requireFieldText(formFixture, "x", position.getX().toString());
    requireFieldText(formFixture, "y", position.getY().toString());
    requireFieldText(formFixture, "z", position.getZ().toString());
  }

  @Override
  protected AudioSensor updateObject(AudioSensor original, String uniqueField) {
    return original.toBuilder()
        .name(uniqueField)
        .description("z")
        .preampId("x")
        .hydrophoneId("y")
        .position(Position.builder()
            .x(4f)
            .y(5f)
            .z(6f)
            .build())
        .build();
  }

  @Override
  protected void assertObjectsEqual(AudioSensor expected, AudioSensor actual, boolean checkUUID) {
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    } else {
      assertNotNull(actual.getUuid());
    }

    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getDescription(), actual.getDescription());
    assertEquals(expected.getHydrophoneId(), actual.getHydrophoneId());
    assertEquals(expected.getPreampId(), actual.getPreampId());

    Position expectedPosition = expected.getPosition();
    Position actualPosition = actual.getPosition();
    assertEquals(expectedPosition.getX(), actualPosition.getX());
    assertEquals(expectedPosition.getY(), actualPosition.getY());
    assertEquals(expectedPosition.getZ(), actualPosition.getZ());
  }
}