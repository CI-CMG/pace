package edu.colorado.cires.pace.data.object.dataset.soundPropagationModels;

import edu.colorado.cires.pace.data.object.dataset.base.DetailedPackage;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class DetailedSoundPropagationModelsPackage extends DetailedPackage implements BaseSoundPropagationModelsPackage {
  @Positive
  @NotNull
  private final Float modeledFrequency;
  private final String softwareNames;
  private final String softwareVersions;
  private final String softwareProtocolCitation;
  private final String softwareDescription;
  private final String softwareProcessingDescription;
  private final LocalDateTime audioStartTime;
  private final LocalDateTime audioEndTime;

  @Override
  public DetailedSoundPropagationModelsPackage setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  @Override
  public DetailedSoundPropagationModelsPackage setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}
