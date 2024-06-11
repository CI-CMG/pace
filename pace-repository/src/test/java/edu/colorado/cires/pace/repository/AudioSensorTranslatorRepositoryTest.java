package edu.colorado.cires.pace.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.translator.AudioSensorTranslator;
import edu.colorado.cires.pace.data.translator.PositionTranslator;
import edu.colorado.cires.pace.data.translator.Translator;

public class AudioSensorTranslatorRepositoryTest extends TranslatorRepositoryTest {

  @Override
  protected Translator createNewObject(int suffix) {
    return AudioSensorTranslator.builder()
        .name(String.format("name-%s", suffix))
        .sensorUUID(String.format("uuid-%s", suffix))
        .sensorName(String.format("name-%s", suffix))
        .description(String.format("description-%s", suffix))
        .positionTranslator(PositionTranslator.builder()
            .x(String.format("x-%s", suffix))
            .y(String.format("y-%s", suffix))
            .z(String.format("z-%s", suffix))
            .build())
        .preampId(String.format("preamp-id-%s", suffix))
        .hydrophoneId(String.format("hydrophone-id-%s", suffix))
        .build();
  }

  @Override
  protected Translator copyWithUpdatedUniqueField(Translator object, String uniqueField) {
    return ((AudioSensorTranslator) object).toBuilder()
        .name(uniqueField)
        .build();
  }

  @Override
  protected void assertObjectsEqual(Translator expected, Translator actual, boolean checkUUID) {
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    }
    assertEquals(expected.getName(), actual.getName());
    
    AudioSensorTranslator expectedSensorTranslator = (AudioSensorTranslator) expected;
    AudioSensorTranslator actualSensorTranslator = (AudioSensorTranslator) actual;
    assertEquals(expectedSensorTranslator.getSensorUUID(), actualSensorTranslator.getSensorUUID());
    assertEquals(expectedSensorTranslator.getSensorName(), actualSensorTranslator.getSensorName());
    assertEquals(expectedSensorTranslator.getDescription(), actualSensorTranslator.getDescription());
    assertEquals(expectedSensorTranslator.getHydrophoneId(), actualSensorTranslator.getHydrophoneId());
    assertEquals(expectedSensorTranslator.getPreampId(), actualSensorTranslator.getPreampId());
    
    PositionTranslator expectedPositionTranslator = expectedSensorTranslator.getPositionTranslator();
    PositionTranslator actualPositionTranslator = actualSensorTranslator.getPositionTranslator();
    assertEquals(expectedPositionTranslator.getX(), actualPositionTranslator.getX());
    assertEquals(expectedPositionTranslator.getY(), actualPositionTranslator.getY());
    assertEquals(expectedPositionTranslator.getZ(), actualPositionTranslator.getZ());
  }
}
