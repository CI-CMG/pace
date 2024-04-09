package edu.colorado.cires.pace.data;

import java.util.List;
import java.util.UUID;

public record ExcelTranslator(UUID uuid, String name, List<ExcelTranslatorField> fields) implements TabularTranslator<ExcelTranslatorField> {

  @Override
  public ObjectWithUUID copyWithNewUUID(UUID uuid) {
    return new ExcelTranslator(
        uuid,
        name,
        fields
    );
  }
}
