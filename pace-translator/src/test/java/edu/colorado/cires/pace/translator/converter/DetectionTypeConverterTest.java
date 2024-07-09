package edu.colorado.cires.pace.translator.converter;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.cires.pace.data.object.DetectionType;
import edu.colorado.cires.pace.data.translator.DetectionTypeTranslator;
import edu.colorado.cires.pace.translator.TranslationException;
import edu.colorado.cires.pace.translator.ValueWithColumnNumber;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class DetectionTypeConverterTest {
  
  private final Converter<DetectionTypeTranslator, DetectionType> converter = new DetectionTypeConverter();

  @Test
  void convert() throws TranslationException {
    UUID uuid = UUID.randomUUID();
    String source = "source-value";
    String scienceName = "science-name-value";
    
    DetectionType detectionType = converter.convert(
        DetectionTypeTranslator.builder()
            .detectionTypeUUID("detection-type-uuid")
            .source("source")
            .scienceName("science-name")
            .build(),
        Map.of(
            "detection-type-uuid", new ValueWithColumnNumber(Optional.of(uuid.toString()), 1),
            "source", new ValueWithColumnNumber(Optional.of(source), 2),
            "science-name", new ValueWithColumnNumber(Optional.of(scienceName), 3)
        ),
        1,
        new RuntimeException()
    );
    
    assertEquals(uuid, detectionType.getUuid());
    assertEquals(source, detectionType.getSource());
    assertEquals(scienceName, detectionType.getScienceName());
  }
}