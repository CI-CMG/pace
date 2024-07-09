package edu.colorado.cires.pace.translator.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.object.Sea;
import edu.colorado.cires.pace.data.translator.SeaTranslator;
import edu.colorado.cires.pace.translator.TranslationException;
import edu.colorado.cires.pace.translator.ValueWithColumnNumber;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class SeaConverterTest {
  
  private final Converter<SeaTranslator, Sea> converter = new SeaConverter();

  @Test
  void convert() throws TranslationException {
    UUID uuid = UUID.randomUUID();
    String name = "name-value";
    
    Sea sea = converter.convert(
        SeaTranslator.builder()
            .seaUUID("UUID")
            .seaName("_NAME")
            .build(),
        Map.of(
            "UUID", new ValueWithColumnNumber(Optional.of(uuid.toString()), 1),
            "_NAME", new ValueWithColumnNumber(Optional.of(name), 2)
        ),
        1,
        new RuntimeException()
    );
    
    assertEquals(uuid, sea.getUuid());
    assertEquals(name, sea.getName());
  }
}