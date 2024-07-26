package edu.colorado.cires.pace.data.object.dataset.base.metadata.translator;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class DefaultTimeTranslator implements TimeTranslator {
  
  private final String time;
  private final String timeZone;

}
