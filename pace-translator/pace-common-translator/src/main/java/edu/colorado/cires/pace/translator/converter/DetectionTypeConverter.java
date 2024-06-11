package edu.colorado.cires.pace.translator.converter;

import static edu.colorado.cires.pace.translator.converter.ConversionUtils.stringFromMap;
import static edu.colorado.cires.pace.translator.converter.ConversionUtils.uuidFromMap;

import edu.colorado.cires.pace.data.object.DetectionType;
import edu.colorado.cires.pace.data.translator.DetectionTypeTranslator;
import edu.colorado.cires.pace.translator.TranslatorExecutor.ValueWithColumnNumber;
import java.util.Map;

public class DetectionTypeConverter extends Converter<DetectionTypeTranslator, DetectionType> {

  @Override
  public DetectionType convert(DetectionTypeTranslator translator, Map<String, ValueWithColumnNumber> properties, int row,
      RuntimeException runtimeException) {
    return DetectionType.builder()
        .uuid(uuidFromMap(properties, translator.getDetectionTypeUUID(), row, runtimeException))
        .source(stringFromMap(properties, translator.getSource()))
        .scienceName(stringFromMap(properties, translator.getScienceName()))
        .build();
  }
}
