package edu.colorado.cires.pace.data.object;

import jakarta.validation.constraints.NotBlank;
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
}
