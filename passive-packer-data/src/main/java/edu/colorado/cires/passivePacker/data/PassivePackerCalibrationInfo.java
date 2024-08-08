package edu.colorado.cires.passivePacker.data;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode
@SuperBuilder(toBuilder = true)
@Jacksonized
public class PassivePackerCalibrationInfo {
  
  private final String calState;
  @Builder.Default
  private final String calDocsPath = "";
  private final String comment;
  private final String calDate;
  private final String calDate2;
  @Builder.Default
  private final String sensitivity = "";
  @Builder.Default
  private final String frequency = "";
  @Builder.Default
  private final String gain = "";

}
