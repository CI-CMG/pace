package edu.colorado.cires.pace.data.object.dataset.passivePacker;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode
@SuperBuilder(toBuilder = true)
@Jacksonized
@JsonTypeInfo(use = Id.DEDUCTION)
@JsonSubTypes({
    @Type(PassivePackerAudioCalibrationInfo.class)
})
public class PassivePackerCalibrationInfo {
  
  private final String calState;
  private final String calDocsPath;
  private final String comment;
  private final LocalDate calDate;

}
