package edu.colorado.cires.pace.data.object.dataset.audio;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.Channel;
import edu.colorado.cires.pace.data.object.dataset.base.DetailedPackage;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.PackageSensor;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DataQualityEntry;
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
public abstract class DetailedAudioDataPackage extends DetailedPackage implements BaseAudioDataPackage<AbstractObject> {

  private final String instrumentId;
  private final LocalDateTime deploymentTime;
  private final LocalDateTime recoveryTime;
  private final String comments;
  @NotEmpty
  @NotNull
  private final List<@NotNull @Valid PackageSensor<AbstractObject>> sensors;
  @NotEmpty @NotNull
  private final List<@Valid @NotNull Channel<AbstractObject>> channels;
  private final Float hydrophoneSensitivity;
  private final Float frequencyRange;
  private final Float gain;
  private final AbstractObject qualityAnalyst;
  private final String qualityAnalysisObjectives;
  private final String qualityAnalysisMethod;
  private final String qualityAssessmentDescription;
  private final List<DataQualityEntry> qualityEntries;

}
