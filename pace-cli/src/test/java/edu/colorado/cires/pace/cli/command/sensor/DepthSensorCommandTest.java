package edu.colorado.cires.pace.cli.command.sensor;

import edu.colorado.cires.pace.data.object.DepthSensor;
import edu.colorado.cires.pace.data.object.Position;
import edu.colorado.cires.pace.data.translator.DepthSensorTranslator;
import edu.colorado.cires.pace.data.translator.PositionTranslator;

class DepthSensorCommandTest extends SensorCommandTest<DepthSensor, DepthSensorTranslator> {

  @Override
  public DepthSensor createObject(String uniqueField) {
    return DepthSensor.builder()
        .name(uniqueField)
        .position(Position.builder()
            .x(1f)
            .y(2f)
            .z(3f)
            .build())
        .description("description")
        .build();
  }

  @Override
  protected DepthSensor updateObject(DepthSensor original, String uniqueField) {
    return original.toBuilder()
        .name(uniqueField)
        .build();
  }

  @Override
  protected void assertSensorTypeSpecificFields(DepthSensor expected, DepthSensor actual) {
    
  }

  @Override
  protected String[] getTranslatorFields() {
    return new String[] {
        "sensorUUID",
        "sensorName",
        "description",
        "position (X)",
        "position (Y)",
        "position (Z)",
    };
  }

  @Override
  protected DepthSensorTranslator createTranslator(String name) {
    return DepthSensorTranslator.builder()
        .name(name)
        .sensorUUID("sensorUUID")
        .sensorName("sensorName")
        .description("description")
        .positionTranslator(PositionTranslator.builder()
            .x("position (X)")
            .y("position (Y)")
            .z("position (Z)")
            .build())
        .build();
  }

  @Override
  protected String[] objectToRow(DepthSensor object) {
    return new String[] {
        object.getUuid() == null ? "" : object.getUuid().toString(),
        object.getName(),
        object.getDescription(),
        object.getPosition().getX().toString(),
        object.getPosition().getY().toString(),
        object.getPosition().getZ().toString()
    };
  }
}
