package edu.colorado.cires.pace.data.object.dataset.soundPropagationModels;

import edu.colorado.cires.pace.data.object.dataset.base.Package;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.TimeRange;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * SoundPropagationModelsPackage extends Package and holds on to fields relevant to
 * sound propagation models
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class SoundPropagationModelsPackage extends Package implements BaseSoundPropagationModelsPackage, TimeRange {
  @Positive @NotNull
  private final Float modeledFrequency;
  private final String softwareNames;
  private final String softwareVersions;
  private final String softwareProtocolCitation;
  private final String softwareDescription;
  private final String softwareProcessingDescription;
  private final LocalDateTime audioStartTime;
  private final LocalDateTime audioEndTime;
  private final LocalDateTime startTime;
  private final LocalDateTime endTime;

  /**
   * Returns package with provided uuid applied
   *
   * @param uuid field for assigning uuid to new object
   * @return SoundPropagationModelsPackage with applied uuid
   */
  @Override
  public SoundPropagationModelsPackage setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  /**
   * Returns package with provided visibility applied
   *
   * @param visible boolean which indicates whether to make the object visible
   *                or invisible
   * @return SoundPropagationModelsPackage with applied visibility
   */
  @Override
  public SoundPropagationModelsPackage setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}
