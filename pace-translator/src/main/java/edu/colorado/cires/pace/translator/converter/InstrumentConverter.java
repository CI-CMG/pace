package edu.colorado.cires.pace.translator.converter;

import static edu.colorado.cires.pace.translator.converter.ConversionUtils.delimitedObjectsFromMap;
import static edu.colorado.cires.pace.translator.converter.ConversionUtils.stringFromMap;
import static edu.colorado.cires.pace.translator.converter.ConversionUtils.uuidFromMap;

import edu.colorado.cires.pace.data.object.Instrument;
import edu.colorado.cires.pace.data.translator.InstrumentTranslator;
import edu.colorado.cires.pace.repository.FileTypeRepository;
import edu.colorado.cires.pace.translator.ValueWithColumnNumber;
import java.util.Map;

public class InstrumentConverter extends Converter<InstrumentTranslator, Instrument> {
  
  private final FileTypeRepository fileTypeRepository;

  public InstrumentConverter(FileTypeRepository fileTypeRepository) {
    this.fileTypeRepository = fileTypeRepository;
  }

  @Override
  public Instrument convert(InstrumentTranslator translator, Map<String, ValueWithColumnNumber> properties, int row,
      RuntimeException runtimeException) {
    return Instrument.builder()
        .uuid(uuidFromMap(properties, translator.getInstrumentUUID(), row, runtimeException))
        .name(stringFromMap(properties, translator.getInstrumentName()))
        .fileTypes(delimitedObjectsFromMap(properties, translator.getFileTypes(), row, runtimeException, fileTypeRepository))
        .build();
  }
}
