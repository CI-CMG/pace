package edu.colorado.cires.pace.data.object.dataset.soundClips;

import edu.colorado.cires.pace.data.object.dataset.base.Package;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.TimeRange;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * SoundClipsPackage extends Package and holds onto software, start and end time,
 * and audio start and end time values
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class SoundClipsPackage extends Package implements BaseSoundClipsPackage, TimeRange {
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
   * Returns package with uuid set to provided
   *
   * @param uuid field for assigning uuid to new object
   * @return SoundClipsPackage with the uuid set as provided
   */
  @Override
  public SoundClipsPackage setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  /**
   * Returns package with visibility set to provided
   *
   * @param visible boolean which indicates whether to make the object visible
   *                or invisible
   * @return SoundClipsPackage with the visibility set as provided
   */
  @Override
  public SoundClipsPackage setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}
