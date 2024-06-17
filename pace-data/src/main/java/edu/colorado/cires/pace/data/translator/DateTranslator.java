package edu.colorado.cires.pace.data.translator;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@SuperBuilder(toBuilder = true)
@Jacksonized
public class DateTranslator implements TimeTranslator {
  
  private final String date;
  private final String timeZone;

}
