package edu.colorado.cires.pace.data.object;

import edu.colorado.cires.pace.data.validation.FileTypeValidator;
import edu.colorado.cires.pace.data.validation.ValidationException;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
public class FileType implements ObjectWithUniqueField {

  final UUID uuid;
  final String type;
  final String comment;

  @Builder(toBuilder = true)
  @Jacksonized
  private FileType(UUID uuid, String type, String comment) throws ValidationException {
    this.uuid = uuid;
    this.type = type;
    this.comment = comment;
    
    new FileTypeValidator().validate(this);
  }
}
