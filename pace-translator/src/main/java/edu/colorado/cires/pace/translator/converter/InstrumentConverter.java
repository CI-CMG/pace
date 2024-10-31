package edu.colorado.cires.pace.translator.converter;

import static edu.colorado.cires.pace.translator.converter.ConversionUtils.stringFromMap;
import static edu.colorado.cires.pace.translator.converter.ConversionUtils.stringListFromMap;
import static edu.colorado.cires.pace.translator.converter.ConversionUtils.uuidFromMap;

import edu.colorado.cires.pace.data.object.instrument.Instrument;
import edu.colorado.cires.pace.data.object.instrument.translator.InstrumentTranslator;
import edu.colorado.cires.pace.translator.ValueWithColumnNumber;
import java.util.Map;

/**
 * InstrumentConverter extends Converter and provides convert function for
 * Instrument objects
 */
public class InstrumentConverter extends Converter<InstrumentTranslator, Instrument> {

  /**
   * Creates an instrument object from the provided properties
   * @param translator translates to instrument object
   * @param properties maps property names to values
   * @param row relevant row
   * @param runtimeException thrown in case of error mapping
   * @return Instrument object
   */
  @Override
  public Instrument convert(InstrumentTranslator translator, Map<String, ValueWithColumnNumber> properties, int row,
      RuntimeException runtimeException) {
    return Instrument.builder()
        .uuid(uuidFromMap(properties, "UUID", translator.getInstrumentUUID(), row, runtimeException))
        .name(stringFromMap(properties, translator.getInstrumentName()))
        .fileTypes(stringListFromMap(properties, translator.getFileTypes()))
        .build();
  }
}
