package edu.colorado.cires.pace.data.object.dataset.passivePacker;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode
@SuperBuilder(toBuilder = true)
@Jacksonized
public class PassivePackerTimeRange {
  
  private final LocalDateTime start;
  private final LocalDateTime end;

}
