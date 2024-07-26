package edu.colorado.cires.pace.data.object.dataset.audio;

import edu.colorado.cires.pace.data.object.dataset.base.Package;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.Channel;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.DataQuality;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DataQualityEntry;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.PackageSensor;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public abstract class AudioDataPackage extends Package implements DataQuality {
  private final String instrumentId;
  private final LocalDateTime deploymentTime;
  private final LocalDateTime recoveryTime;
  private final String comments;
  @NotEmpty @NotNull
  private final List<@NotNull @Valid PackageSensor> sensors;
  @NotEmpty @NotNull
  private final List<@Valid @NotNull Channel> channels;
  private final Float hydrophoneSensitivity;
  private final Float frequencyRange;
  private final Float gain;
  private final String qualityAnalyst;
  private final String qualityAnalysisObjectives;
  private final String qualityAnalysisMethod;
  private final String qualityAssessmentDescription;
  private final List<DataQualityEntry> qualityEntries;
  
  public abstract AudioDataPackage updateChannels(List<Channel> channels);
  public abstract AudioDataPackage updateSensors(List<PackageSensor> sensors);
}