package edu.colorado.cires.pace.translator.converter;

import static edu.colorado.cires.pace.translator.converter.ConversionUtils.floatFromMap;
import static edu.colorado.cires.pace.translator.converter.ConversionUtils.stringFromMap;
import static edu.colorado.cires.pace.translator.converter.ConversionUtils.uuidFromMap;

import edu.colorado.cires.pace.data.object.AudioSensor;
import edu.colorado.cires.pace.data.object.DepthSensor;
import edu.colorado.cires.pace.data.object.OtherSensor;
import edu.colorado.cires.pace.data.object.Position;
import edu.colorado.cires.pace.data.object.Sensor;
import edu.colorado.cires.pace.data.translator.AudioSensorTranslator;
import edu.colorado.cires.pace.data.translator.DepthSensorTranslator;
import edu.colorado.cires.pace.data.translator.OtherSensorTranslator;
import edu.colorado.cires.pace.data.translator.PositionTranslator;
import edu.colorado.cires.pace.data.translator.SensorTranslator;
import edu.colorado.cires.pace.translator.TranslationException;
import edu.colorado.cires.pace.translator.ValueWithColumnNumber;
import java.util.Map;

public class SensorConverter extends Converter<SensorTranslator, Sensor> {

  @Override
  public Sensor convert(SensorTranslator translator, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException)
      throws TranslationException {
    return sensorFromMap(translator, properties, row, runtimeException);
  }

  public static Sensor sensorFromMap(SensorTranslator sensorTranslator, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException)
      throws TranslationException {
    if (sensorTranslator instanceof DepthSensorTranslator depthSensorTranslator) {
      return depthSensorFromMap(depthSensorTranslator, properties, row, runtimeException);
    } else if (sensorTranslator instanceof OtherSensorTranslator otherSensorTranslator) {
      return otherSensorFromMap(otherSensorTranslator, properties, row, runtimeException);
    } else if (sensorTranslator instanceof AudioSensorTranslator audioSensorTranslator) {
      return audioSensorFromMap(audioSensorTranslator, properties, row, runtimeException);
    } else {
      throw new TranslationException(String.format(
          "Translation not supported for %s", sensorTranslator.getClass().getSimpleName()
      ));
    }
  }

  private static Position positionFromMap(PositionTranslator positionTranslator, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException) {
    return Position.builder()
        .x(floatFromMap(properties, positionTranslator.getX(), row, runtimeException))
        .y(floatFromMap(properties, positionTranslator.getY(), row, runtimeException))
        .z(floatFromMap(properties, positionTranslator.getZ(), row, runtimeException))
        .build();
  }

  private static DepthSensor depthSensorFromMap(DepthSensorTranslator depthSensorTranslator, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException) {
    return DepthSensor.builder()
        .uuid(uuidFromMap(properties, depthSensorTranslator.getSensorUUID(), row, runtimeException))
        .name(stringFromMap(properties, depthSensorTranslator.getSensorName()))
        .position(positionFromMap(depthSensorTranslator.getPositionTranslator(), properties, row, runtimeException))
        .description(stringFromMap(properties, depthSensorTranslator.getDescription()))
        .build();
  }

  private static AudioSensor audioSensorFromMap(AudioSensorTranslator audioSensorTranslator, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException) {
    return AudioSensor.builder()
        .uuid(uuidFromMap(properties, audioSensorTranslator.getSensorUUID(), row, runtimeException))
        .name(stringFromMap(properties, audioSensorTranslator.getSensorName()))
        .position(positionFromMap(audioSensorTranslator.getPositionTranslator(), properties, row, runtimeException))
        .description(stringFromMap(properties, audioSensorTranslator.getDescription()))
        .hydrophoneId(stringFromMap(properties, audioSensorTranslator.getHydrophoneId()))
        .preampId(stringFromMap(properties, audioSensorTranslator.getPreampId()))
        .build();
  }

  private static OtherSensor otherSensorFromMap(OtherSensorTranslator otherSensorTranslator, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException) {
    return OtherSensor.builder()
        .uuid(uuidFromMap(properties, otherSensorTranslator.getSensorUUID(), row, runtimeException))
        .name(stringFromMap(properties, otherSensorTranslator.getSensorName()))
        .position(positionFromMap(otherSensorTranslator.getPositionTranslator(), properties, row, runtimeException))
        .description(stringFromMap(properties, otherSensorTranslator.getDescription()))
        .properties(stringFromMap(properties, otherSensorTranslator.getProperties()))
        .sensorType(stringFromMap(properties, otherSensorTranslator.getSensorType()))
        .build();
  }

}
