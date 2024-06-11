package edu.colorado.cires.pace.data.translator;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class InstrumentTranslator implements Translator {
  
  private final UUID uuid;
  private final String name;
  
  private final String instrumentUUID;
  private final String instrumentName;
  private final String fileTypes;

}
