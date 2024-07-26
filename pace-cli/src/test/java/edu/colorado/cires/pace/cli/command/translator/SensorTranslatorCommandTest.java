package edu.colorado.cires.pace.cli.command.translator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.object.sensor.base.translator.SensorTranslator;
import java.util.UUID;

abstract class SensorTranslatorCommandTest<S extends SensorTranslator> extends TranslatorCommandTest<S> {

  @Override
  protected void assertTranslatorTypeSpecificFields(S expected, S actual) {
    assertEquals(expected.getSensorName(), actual.getSensorName());
    assertEquals(expected.getSensorUUID(), actual.getSensorUUID());
    assertEquals(expected.getDescription(), actual.getDescription());
    
    assertSensorTranslatorTypeSpecificFields(expected, actual);
  }

  @Override
  public S createObject(String uniqueField, boolean withUUID) {
    return addSensorTypeSpecificFields(
        SensorTranslator.builder()
            .uuid(withUUID ? UUID.randomUUID() : null)
            .name(uniqueField)
            .description("description")
            .sensorName("sensorName")
            .sensorUUID("sensorUUID")
            .build()
    );
  }
  
  protected abstract void assertSensorTranslatorTypeSpecificFields(S expected, S actual);
  protected abstract S addSensorTypeSpecificFields(SensorTranslator sensorTranslator);
}
