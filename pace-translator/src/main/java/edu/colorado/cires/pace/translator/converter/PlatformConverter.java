package edu.colorado.cires.pace.translator.converter;

import static edu.colorado.cires.pace.translator.converter.ConversionUtils.stringFromMap;
import static edu.colorado.cires.pace.translator.converter.ConversionUtils.uuidFromMap;

import edu.colorado.cires.pace.data.object.Platform;
import edu.colorado.cires.pace.data.translator.PlatformTranslator;
import edu.colorado.cires.pace.translator.ValueWithColumnNumber;
import java.util.Map;

public class PlatformConverter extends Converter<PlatformTranslator, Platform> {

  @Override
  public Platform convert(PlatformTranslator translator, Map<String, ValueWithColumnNumber> properties, int row,
      RuntimeException runtimeException) {
    return Platform.builder()
        .uuid(uuidFromMap(properties, translator.getPlatformUUID(), row, runtimeException))
        .name(stringFromMap(properties, translator.getPlatformName()))
        .build();
  }
}
