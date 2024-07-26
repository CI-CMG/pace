package edu.colorado.cires.pace.translator.converter;

import static edu.colorado.cires.pace.translator.converter.ConversionUtils.stringFromMap;
import static edu.colorado.cires.pace.translator.converter.ConversionUtils.uuidFromMap;

import edu.colorado.cires.pace.data.object.sea.Sea;
import edu.colorado.cires.pace.data.object.sea.translator.SeaTranslator;
import edu.colorado.cires.pace.translator.ValueWithColumnNumber;
import java.util.Map;

public class SeaConverter extends Converter<SeaTranslator, Sea> {

  @Override
  public Sea convert(SeaTranslator translator, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException) {
    return Sea.builder()
        .uuid(uuidFromMap(properties, translator.getSeaUUID(), row, runtimeException))
        .name(stringFromMap(properties, translator.getSeaName()))
        .build();
  }
}
