package edu.colorado.cires.pace.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.translator.OtherSensorTranslator;
import edu.colorado.cires.pace.data.translator.PositionTranslator;
import edu.colorado.cires.pace.data.translator.Translator;

public class OtherSensorTranslatorRepositoryTest extends TranslatorRepositoryTest {

  @Override
  protected String getUniqueFieldName() {
    return "name";
  }

  @Override
  protected Translator createNewObject(int suffix) {
    return OtherSensorTranslator.builder()
        .name(String.format("name-%s", suffix))
        .sensorUUID(String.format("uuid-%s", suffix))
        .sensorName(String.format("name-%s", suffix))
        .description(String.format("description-%s", suffix))
        .positionTranslator(PositionTranslator.builder()
            .x(String.format("x-%s", suffix))
            .y(String.format("y-%s", suffix))
            .z(String.format("z-%s", suffix))
            .build())
        .properties(String.format("properties-%s", suffix))
        .sensorType(String.format("sensor-type-%s", suffix))
        .build();
  }

  @Override
  protected Translator copyWithUpdatedUniqueField(Translator object, String uniqueField) {
    return ((OtherSensorTranslator) object).toBuilder()
        .name(uniqueField)
        .build();
  }

  @Override
  protected void assertObjectsEqual(Translator expected, Translator actual, boolean checkUUID) {
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    }
    assertEquals(expected.getName(), actual.getName());

    OtherSensorTranslator expectedSensorTranslator = (OtherSensorTranslator) expected;
    OtherSensorTranslator actualSensorTranslator = (OtherSensorTranslator) actual;
    assertEquals(expectedSensorTranslator.getSensorUUID(), actualSensorTranslator.getSensorUUID());
    assertEquals(expectedSensorTranslator.getSensorName(), actualSensorTranslator.getSensorName());
    assertEquals(expectedSensorTranslator.getDescription(), actualSensorTranslator.getDescription());
    assertEquals(expectedSensorTranslator.getProperties(), actualSensorTranslator.getProperties());
    assertEquals(expectedSensorTranslator.getSensorType(), actualSensorTranslator.getSensorType());

    PositionTranslator expectedPositionTranslator = expectedSensorTranslator.getPositionTranslator();
    PositionTranslator actualPositionTranslator = actualSensorTranslator.getPositionTranslator();
    assertEquals(expectedPositionTranslator.getX(), actualPositionTranslator.getX());
    assertEquals(expectedPositionTranslator.getY(), actualPositionTranslator.getY());
    assertEquals(expectedPositionTranslator.getZ(), actualPositionTranslator.getZ());
  }
}
