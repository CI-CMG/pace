package edu.colorado.cires.pace.data.object.dataset.passivePacker;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class PassivePackerAudioDeployment extends PassivePackerDeployment {

  private final LocalDateTime deploymentTime;
  private final LocalDateTime recoveryTime;
  private final LocalDateTime audioStart;
  private final LocalDateTime audioEnd;

}
