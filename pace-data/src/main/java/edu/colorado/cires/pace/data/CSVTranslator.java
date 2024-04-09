package edu.colorado.cires.pace.data;

import java.util.List;
import java.util.UUID;

public record CSVTranslator(UUID uuid, String name, List<CSVTranslatorField> fields) implements TabularTranslator<CSVTranslatorField> {

  @Override
  public ObjectWithUUID copyWithNewUUID(UUID uuid) {
    return new CSVTranslator(
        uuid,
        name,
        fields
    );
  }
}
