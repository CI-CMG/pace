package edu.colorado.cires.pace.data.object.dataset.base.metadata.translator;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class DateTranslator {
  
  private final String date;
  private final String timeZone;

}
