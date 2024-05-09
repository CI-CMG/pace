package edu.colorado.cires.pace.data.object;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@SuperBuilder(toBuilder = true)
@Jacksonized
public class CPodDataset implements Dataset, AudioDatasetDetail, AudioCalibrationDetail {
  
  private final String siteOrCruiseName;
  private final String deploymentId;
  private final Person datasetPackager;
  private final List<Project> projects;
  private final LocalDate publicReleaseDate;

  private final List<Person> scientists;
  private final List<Organization> sponsors;
  private final List<Organization> funders;

  private final Platform platform;
  private final Instrument instrument;
  private final String instrumentId;
  private final LocalDateTime startTime;
  private final LocalDateTime endTime;

  private final LocalDate preDeploymentCalibrationDate;
  private final LocalDate postDeploymentCalibrationDate;
  private final String calibrationDescription;
  private final Float hydrophoneSensitivity;
  private final Float frequencyRange;
  private final Float gain;

  private final String deploymentTitle;
  private final String deploymentPurpose;
  private final String deploymentDescription;
  private final String alternateSiteName;
  private final String alternateDeploymentName;
  private final Person qualityAnalyst;
  private final String qualityAnalysisObjectives;
  private final String qualityAnalysisMethod;
  private final String qualityAssessmentDescription;
  private final List<DataQualityEntry> qualityEntries;
  private final LocalDateTime deploymentTime;
  private final LocalDateTime recoveryTime;
  private final String comments;
  private final List<Sensor> sensors;
  private final List<Channel> channels;
  
  private final LocationDetail locationDetail;
  
}
