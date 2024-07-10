package edu.colorado.cires.pace.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import edu.colorado.cires.pace.data.object.DepthSensor;
import edu.colorado.cires.pace.data.object.Position;
import org.assertj.swing.fixture.JPanelFixture;

class DepthSensorPanelTest extends SensorsPanelTest<DepthSensor> {

  @Override
  protected void fillOutForm(JPanelFixture formFixture, DepthSensor object) {
    if (object.getUuid() == null) {
      formFixture.comboBox()
          .requireVisible()
          .requireEnabled()
          .requireItemCount(3)
          .selectItem("depth");
    }
    
    enterFieldText(formFixture, "name", object.getName());
    enterFieldText(formFixture,  "description", object.getDescription());
    Position position = object.getPosition();
    enterFieldText(formFixture, "x", position.getX().toString());
    enterFieldText(formFixture, "y", position.getY().toString());
    enterFieldText(formFixture, "z", position.getZ().toString());
  }

  @Override
  protected DepthSensor createObject(String uniqueField) {
    return DepthSensor.builder()
        .name(uniqueField)
        .description("a")
        .position(Position.builder()
            .x(1f)
            .y(2f)
            .z(3f)
            .build())
        .build();
  }

  @Override
  protected void requireFormContents(JPanelFixture formFixture, DepthSensor object) {
    requireFieldText(formFixture, "uuid", object.getUuid().toString());
    requireFieldText(formFixture,  "name", object.getName());
    requireFieldText(formFixture, "description", object.getDescription());
    
    JPanelFixture positionForm = getPanel(formFixture, "positionForm");
    Position position = object.getPosition();
    requireFieldText(positionForm, "x", position.getX().toString());
    requireFieldText(positionForm, "y", position.getY().toString());
    requireFieldText(positionForm, "z", position.getZ().toString());
  }

  @Override
  protected DepthSensor updateObject(DepthSensor original, String uniqueField) {
    return original.toBuilder()
        .name(uniqueField)
        .description("z")
        .position(Position.builder()
            .x(4f)
            .y(5f)
            .z(6f)
            .build())
        .build();
  }

  @Override
  protected void assertObjectsEqual(DepthSensor expected, DepthSensor actual, boolean checkUUID) {
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    } else {
      assertNotNull(actual.getUuid());
    }
    
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getDescription(), actual.getDescription());

    Position expectedPosition = expected.getPosition();
    Position actualPosition = actual.getPosition();
    assertEquals(expectedPosition.getX(), actualPosition.getX());
    assertEquals(expectedPosition.getY(), actualPosition.getY());
    assertEquals(expectedPosition.getZ(), actualPosition.getZ());
  }
}
