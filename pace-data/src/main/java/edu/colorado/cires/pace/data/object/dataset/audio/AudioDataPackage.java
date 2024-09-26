package edu.colorado.cires.pace.data.object.dataset.audio;

import edu.colorado.cires.pace.data.object.dataset.base.Package;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.Channel;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DataQualityEntry;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.PackageSensor;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * AudioDataPackage holds all the data for an audio type package
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public class AudioDataPackage extends Package implements BaseAudioDataPackage<String> {
  private final String instrumentId;
  private final LocalDateTime deploymentTime;
  private final LocalDateTime recoveryTime;
  private final LocalDateTime audioStartTime;
  private final LocalDateTime audioEndTime;
  private final String comments;
  @Builder.Default
  private final List<@NotNull @Valid PackageSensor<String>> sensors = Collections.emptyList();
  @NotEmpty @NotNull @Builder.Default
  private final List<@Valid @NotNull Channel<String>> channels = Collections.emptyList();
  private final String qualityAnalyst;
  private final String qualityAnalysisObjectives;
  private final String qualityAnalysisMethod;
  private final String qualityAssessmentDescription;
  @Builder.Default
  private final List<DataQualityEntry> qualityEntries = Collections.emptyList();

  /**
   * Returns a new AudioDataPackage with the provided channels
   *
   * @param channels the updated list of channels that applies to the package
   * @return the package with the updated list of channels
   */
  @Override
  public AudioDataPackage updateChannels(List<Channel<String>> channels) {
    return toBuilder().channels(channels).build();
  }

  /**
   * Returns a new AudioDataPackage with the provided list of sensors
   *
   * @param packageSensors the updated list of sensors that applies to the package
   * @return the package with the updated list of sensors
   */
  @Override
  public AudioDataPackage updateSensors(List<PackageSensor<String>> packageSensors) {
    return toBuilder().sensors(packageSensors).build();
  }

  /**
   * Returns a new AudioDataPackage with the provided quality analyst
   *
   * @param qualityAnalyst the quality analyst to apply to the returned package
   * @return the package with the provided quality analyst
   */
  @Override
  public AudioDataPackage setQualityAnalyst(String qualityAnalyst) {
    return toBuilder().qualityAnalyst(qualityAnalyst).build();
  }
}
