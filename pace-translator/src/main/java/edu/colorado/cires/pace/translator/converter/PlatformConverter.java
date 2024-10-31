package edu.colorado.cires.pace.translator.converter;

import static edu.colorado.cires.pace.translator.converter.ConversionUtils.stringFromMap;
import static edu.colorado.cires.pace.translator.converter.ConversionUtils.uuidFromMap;

import edu.colorado.cires.pace.data.object.platform.Platform;
import edu.colorado.cires.pace.data.object.platform.translator.PlatformTranslator;
import edu.colorado.cires.pace.translator.ValueWithColumnNumber;
import java.util.Map;

/**
 * PlatformConverter extends Converter and provides convert function for
 * Platform objects
 */
public class PlatformConverter extends Converter<PlatformTranslator, Platform> {

  /**
   * Creates a platform object from the provided properties
   * @param translator translates to platform object
   * @param properties maps property names to values
   * @param row relevant row
   * @param runtimeException thrown in case of error mapping
   * @return Platform object
   */
  @Override
  public Platform convert(PlatformTranslator translator, Map<String, ValueWithColumnNumber> properties, int row,
      RuntimeException runtimeException) {
    return Platform.builder()
        .uuid(uuidFromMap(properties, "UUID", translator.getPlatformUUID(), row, runtimeException))
        .name(stringFromMap(properties, translator.getPlatformName()))
        .build();
  }
}
