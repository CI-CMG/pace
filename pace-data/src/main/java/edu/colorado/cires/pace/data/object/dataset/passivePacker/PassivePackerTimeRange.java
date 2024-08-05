package edu.colorado.cires.pace.data.object.dataset.passivePacker;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode
@SuperBuilder(toBuilder = true)
@Jacksonized
public class PassivePackerTimeRange {
  
  private final String start;
  private final String end;

}
