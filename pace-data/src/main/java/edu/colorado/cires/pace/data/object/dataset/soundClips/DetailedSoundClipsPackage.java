package edu.colorado.cires.pace.data.object.dataset.soundClips;

import edu.colorado.cires.pace.data.object.dataset.base.DetailedPackage;
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
public class DetailedSoundClipsPackage extends DetailedPackage implements BaseSoundClipsPackage {
  private final String softwareNames;
  private final String softwareVersions;
  private final String softwareProtocolCitation;
  private final String softwareDescription;
  private final String softwareProcessingDescription;
  private final LocalDateTime audioStartTime;
  private final LocalDateTime audioEndTime;

  @Override
  public DetailedSoundClipsPackage setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  @Override
  public DetailedSoundClipsPackage setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}
