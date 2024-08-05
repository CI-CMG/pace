package edu.colorado.cires.pace.data.object.dataset.passivePacker;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode
@SuperBuilder(toBuilder = true)
@Jacksonized
public class PassivePackerDeployment {

  @Builder.Default
  private final String deploymentTime = "";
  @Builder.Default
  private final String recoveryTime = "";
  @Builder.Default
  private final String audioStart = "";
  @Builder.Default
  private final String audioEnd = "";
  
  @JsonUnwrapped
  private final PassivePackerLocation location;

}
