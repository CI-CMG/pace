package edu.colorado.cires.pace.translator.converter;

import static edu.colorado.cires.pace.translator.converter.ConversionUtils.stringFromMap;
import static edu.colorado.cires.pace.translator.converter.ConversionUtils.uuidFromMap;

import edu.colorado.cires.pace.data.object.sea.Sea;
import edu.colorado.cires.pace.data.object.sea.translator.SeaTranslator;
import edu.colorado.cires.pace.translator.ValueWithColumnNumber;
import java.util.Map;

/**
 * SeaConverter extends Converter and provides convert function for
 * Sea objects
 */
public class SeaConverter extends Converter<SeaTranslator, Sea> {

  /**
   * Creates a sea object from the provided properties
   * @param translator translates to sea object
   * @param properties maps property names to values
   * @param row relevant row
   * @param runtimeException thrown in case of error mapping
   * @return Sea object
   */
  @Override
  public Sea convert(SeaTranslator translator, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException) {
    return Sea.builder()
        .uuid(uuidFromMap(properties, "UUID", translator.getSeaUUID(), row, runtimeException))
        .name(stringFromMap(properties, translator.getSeaName()))
        .build();
  }
}
