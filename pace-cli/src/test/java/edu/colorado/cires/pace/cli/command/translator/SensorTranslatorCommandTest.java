package edu.colorado.cires.pace.cli.command.translator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.translator.PositionTranslator;
import edu.colorado.cires.pace.data.translator.SensorTranslator;

abstract class SensorTranslatorCommandTest<S extends SensorTranslator> extends TranslatorCommandTest<S> {

  @Override
  protected void assertTranslatorTypeSpecificFields(S expected, S actual) {
    assertEquals(expected.getSensorName(), actual.getSensorName());
    assertEquals(expected.getSensorUUID(), actual.getSensorUUID());
    assertEquals(expected.getDescription(), actual.getDescription());
    
    PositionTranslator expectedPositionTranslator = expected.getPositionTranslator();
    PositionTranslator actualPositionTranslator = actual.getPositionTranslator();
    assertEquals(expectedPositionTranslator.getX(), actualPositionTranslator.getX());
    assertEquals(expectedPositionTranslator.getY(), actualPositionTranslator.getY());
    assertEquals(expectedPositionTranslator.getZ(), actualPositionTranslator.getZ());
    
    assertSensorTranslatorTypeSpecificFields(expected, actual);
  }

  @Override
  public S createObject(String uniqueField) {
    return addSensorTypeSpecificFields(
        SensorTranslator.builder()
            .name(uniqueField)
            .description("description")
            .sensorName("sensorName")
            .sensorUUID("sensorUUID")
            .positionTranslator(PositionTranslator.builder()
                .x("X")
                .y("Y")
                .z("Z")
                .build())
            .build()
    );
  }
  
  protected abstract void assertSensorTranslatorTypeSpecificFields(S expected, S actual);
  protected abstract S addSensorTypeSpecificFields(SensorTranslator sensorTranslator);
}
