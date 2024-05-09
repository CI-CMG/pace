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
public class DetectionsDataset implements Dataset, DetectionsDatasetDetail {
  
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
  private final LocalDateTime startTime;
  private final LocalDateTime endTime;
  private final DetectionType soundSource;
  private final Person qualityAnalyst;
  private final String qualityAnalysisObjectives;
  private final String qualityAnalysisMethod;
  private final String qualityAssessmentDescription;
  private final List<DataQualityEntry> qualityEntries;
  private final String softwareNames;
  private final String softwareVersions;
  private final String softwareProtocolCitation;
  private final String softwareDescription;
  private final String softwareProcessingDescription;
  private final Integer analysisTimeZone;
  private final Integer analysisEffort;
  private final Float sampleRate;
  private final Float minFrequency;
  private final Float maxFrequency;
  
  private final LocalDate preDeploymentCalibrationDate;
  private final LocalDate postDeploymentCalibrationDate;
  private final String calibrationDescription;
  
  private final String deploymentTitle;
  private final String deploymentPurpose;
  private final String deploymentDescription;
  private final String alternateSiteName;
  private final String alternateDeploymentName;
  
  private final LocationDetail locationDetail;
  
}
