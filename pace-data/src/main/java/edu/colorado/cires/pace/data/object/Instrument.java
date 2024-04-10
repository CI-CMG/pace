package edu.colorado.cires.pace.data.object;

import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Getter
@Jacksonized
public class Instrument implements ObjectWithName {
  
  private final UUID uuid;
  private final String name;
  private final List<FileType> fileTypes;

}
