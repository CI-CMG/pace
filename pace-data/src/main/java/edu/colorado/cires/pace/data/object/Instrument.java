package edu.colorado.cires.pace.data.object;

import edu.colorado.cires.pace.data.validation.InstrumentValidator;
import edu.colorado.cires.pace.data.validation.ValidationException;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
public class Instrument implements ObjectWithName {
  
  private final UUID uuid;
  private final String name;
  private final List<FileType> fileTypes;

  @Builder(toBuilder = true)
  @Jacksonized
  private Instrument(UUID uuid, String name, List<FileType> fileTypes) throws ValidationException {
    this.uuid = uuid;
    this.name = name;
    this.fileTypes = fileTypes;
    
    new InstrumentValidator().validate(this);
  }
}
