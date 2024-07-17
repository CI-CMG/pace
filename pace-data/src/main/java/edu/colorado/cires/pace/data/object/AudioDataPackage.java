package edu.colorado.cires.pace.data.object;

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
  private final List<Sensor> sensors;
  private final List<Channel> channels;
  private final Float hydrophoneSensitivity;
  private final Float frequencyRange;
  private final Float gain;
  private final Person qualityAnalyst;
  private final String qualityAnalysisObjectives;
  private final String qualityAnalysisMethod;
  private final String qualityAssessmentDescription;
  private final List<DataQualityEntry> qualityEntries;
}
