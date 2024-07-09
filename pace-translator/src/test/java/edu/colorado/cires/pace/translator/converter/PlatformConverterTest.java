package edu.colorado.cires.pace.translator.converter;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.cires.pace.data.object.Platform;
import edu.colorado.cires.pace.data.translator.PlatformTranslator;
import edu.colorado.cires.pace.translator.TranslationException;
import edu.colorado.cires.pace.translator.ValueWithColumnNumber;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class PlatformConverterTest {
  
  private final Converter<PlatformTranslator, Platform> converter = new PlatformConverter();

  @Test
  void convert() throws TranslationException {
    UUID uuid = UUID.randomUUID();
    String name = "name-value";
    
    Platform platform = converter.convert(
        PlatformTranslator.builder()
            .platformUUID("UUID")
            .platformName("namE")
            .build(),
        Map.of(
            "UUID", new ValueWithColumnNumber(Optional.of(uuid.toString()), 1),
            "namE", new ValueWithColumnNumber(Optional.of(name), 2)
        ),
        1,
        new RuntimeException()
    );
    
    assertEquals(uuid, platform.getUuid());
    assertEquals(name, platform.getName());
  }
}