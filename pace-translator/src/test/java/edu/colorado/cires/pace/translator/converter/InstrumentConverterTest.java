package edu.colorado.cires.pace.translator.converter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import edu.colorado.cires.pace.data.object.FileType;
import edu.colorado.cires.pace.data.object.Instrument;
import edu.colorado.cires.pace.data.translator.InstrumentTranslator;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.FileTypeRepository;
import edu.colorado.cires.pace.repository.NotFoundException;
import edu.colorado.cires.pace.translator.TranslationException;
import edu.colorado.cires.pace.translator.ValueWithColumnNumber;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class InstrumentConverterTest {
  
  private static final FileTypeRepository fileTypeRepository;
  
  static {
    fileTypeRepository = mock(FileTypeRepository.class);
    try {
      when(fileTypeRepository.getByUniqueField("file-type-1")).thenReturn(
          FileType.builder()
              .type("file-type-1")
              .build()
      );
      when(fileTypeRepository.getByUniqueField("file-type-2")).thenReturn(
          FileType.builder()
              .type("file-type-2")
              .build()
      );
    } catch (DatastoreException | NotFoundException e) {
      throw new RuntimeException(e);
    }
  }
  
  private final Converter<InstrumentTranslator, Instrument> converter = new InstrumentConverter(fileTypeRepository);

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
    assertEquals(Set.of("file-type-1", "file-type-2"), instrument.getFileTypes().stream().map(FileType::getType).collect(Collectors.toSet()));
  }
}