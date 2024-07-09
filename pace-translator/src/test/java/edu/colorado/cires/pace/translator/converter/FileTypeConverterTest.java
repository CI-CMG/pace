package edu.colorado.cires.pace.translator.converter;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.cires.pace.data.object.FileType;
import edu.colorado.cires.pace.data.translator.FileTypeTranslator;
import edu.colorado.cires.pace.translator.TranslationException;
import edu.colorado.cires.pace.translator.ValueWithColumnNumber;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class FileTypeConverterTest {
  
  private final Converter<FileTypeTranslator, FileType> converter = new FileTypeConverter();

  @Test
  void convert() throws TranslationException {
    UUID uuid = UUID.randomUUID();
    String type = "type-value";
    String comment = "comment-value";
    
    FileType fileType = converter.convert(
        FileTypeTranslator.builder()
            .fileTypeUUID("uuid_")
            .type("ft-name")
            .comment("ft-comment")
            .build(),
        Map.of(
            "uuid_", new ValueWithColumnNumber(Optional.of(uuid.toString()), 1),
            "ft-name", new ValueWithColumnNumber(Optional.of(type), 2),
            "ft-comment", new ValueWithColumnNumber(Optional.of(comment), 3)
        ),
        1,
        new RuntimeException()
    );
    assertEquals(uuid, fileType.getUuid());
    assertEquals(type, fileType.getType());
    assertEquals(comment, fileType.getComment());
  }
}