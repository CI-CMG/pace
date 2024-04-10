package edu.colorado.cires.pace.data.object;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class FileType implements ObjectWithUniqueField {

  final UUID uuid;
  final String type;
  final String comment;

  @Override
  public String getUniqueField() {
    return type;
  }
}
