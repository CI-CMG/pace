package edu.colorado.cires.pace.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.translator.OtherSensorTranslator;
import edu.colorado.cires.pace.data.translator.Translator;

public class OtherSensorTranslatorRepositoryTest extends TranslatorRepositoryTest {

  @Override
  protected Translator createNewObject(int suffix) {
    return OtherSensorTranslator.builder()
        .name(String.format("name-%s", suffix))
        .sensorUUID(String.format("uuid-%s", suffix))
        .sensorName(String.format("name-%s", suffix))
        .description(String.format("description-%s", suffix))
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
  }
}
