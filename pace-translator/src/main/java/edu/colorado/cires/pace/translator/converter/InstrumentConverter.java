package edu.colorado.cires.pace.translator.converter;

import static edu.colorado.cires.pace.translator.converter.ConversionUtils.stringFromMap;
import static edu.colorado.cires.pace.translator.converter.ConversionUtils.stringListFromMap;
import static edu.colorado.cires.pace.translator.converter.ConversionUtils.uuidFromMap;

import edu.colorado.cires.pace.data.object.instrument.Instrument;
import edu.colorado.cires.pace.data.object.instrument.translator.InstrumentTranslator;
import edu.colorado.cires.pace.translator.ValueWithColumnNumber;
import java.util.Map;

public class InstrumentConverter extends Converter<InstrumentTranslator, Instrument> {

  @Override
  public Instrument convert(InstrumentTranslator translator, Map<String, ValueWithColumnNumber> properties, int row,
      RuntimeException runtimeException) {
    return Instrument.builder()
        .uuid(uuidFromMap(properties, translator.getInstrumentUUID(), row, runtimeException))
        .name(stringFromMap(properties, translator.getInstrumentName()))
        .fileTypes(stringListFromMap(properties, translator.getFileTypes()))
        .build();
  }
}
