package edu.colorado.cires.pace.data.object.fileType;

import edu.colorado.cires.pace.data.object.base.ObjectWithUniqueField;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class FileType extends ObjectWithUniqueField {

  @NotBlank
  final String type;
  final String comment;

  @Override
  public String getUniqueField() {
    return type;
  }

  @Override
  public ObjectWithUniqueField setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  @Override
  public FileType setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}
