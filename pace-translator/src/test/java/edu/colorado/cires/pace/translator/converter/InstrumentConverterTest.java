package edu.colorado.cires.pace.translator.converter;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.cires.pace.data.object.instrument.Instrument;
import edu.colorado.cires.pace.data.object.instrument.translator.InstrumentTranslator;
import edu.colorado.cires.pace.translator.TranslationException;
import edu.colorado.cires.pace.translator.ValueWithColumnNumber;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class InstrumentConverterTest {
  
  private final Converter<InstrumentTranslator, Instrument> converter = new InstrumentConverter();

  @Test
  void convert() throws TranslationException {
    UUID uuid = UUID.randomUUID();
    String name = "name-value";
    String fileTypes = "file-type-1;file-type-2";
    
    Instrument instrument = converter.convert(
        InstrumentTranslator.builder()
            .instrumentUUID("instrument-uuid")
            .instrumentName("instrument-name")
            .fileTypes("file-types")
            .build(),
        Map.of(
            "instrument-uuid", new ValueWithColumnNumber(Optional.of(uuid.toString()), 1),
            "instrument-name", new ValueWithColumnNumber(Optional.of(name), 2),
            "file-types", new ValueWithColumnNumber(Optional.of(fileTypes), 3)
        ),
        1,
        new RuntimeException()
    );
    
    assertEquals(uuid, instrument.getUuid());
    assertEquals(name, instrument.getName());
    assertEquals(Set.of("file-type-1", "file-type-2"), new HashSet<>(instrument.getFileTypes()));
  }
}