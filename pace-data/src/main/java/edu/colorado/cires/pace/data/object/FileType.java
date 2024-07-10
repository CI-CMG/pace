package edu.colorado.cires.pace.data.object;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class FileType implements ObjectWithUniqueField {

  final UUID uuid;
  @NotBlank
  final String type;
  @Size(min = 1, message = "must not be blank")
  final String comment;
  
}
