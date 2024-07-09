package edu.colorado.cires.pace.data.translator;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class GainTranslator {

  private final TimeTranslator startTime;
  private final TimeTranslator endTime;
  private final String gain;

}
