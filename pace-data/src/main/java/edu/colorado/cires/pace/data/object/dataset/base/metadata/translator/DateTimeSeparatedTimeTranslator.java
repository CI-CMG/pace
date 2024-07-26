package edu.colorado.cires.pace.data.object.dataset.base.metadata.translator;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class DateTimeSeparatedTimeTranslator implements TimeTranslator {
  
  private final String date;
  private final String time;
  private final String timeZone;

}