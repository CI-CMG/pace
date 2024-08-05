package edu.colorado.cires.pace.data.object.dataset.passivePacker;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode
@SuperBuilder(toBuilder = true)
@Jacksonized
public class PassivePackerDatasetDetails {
  
  private final String type;
  private final String subType;
  private final String sourcePath;
  private final String dataFiles;
  @Builder.Default
  private final String analysisTimeZone = "";
  @Builder.Default
  private final String analysisEffort = "";
  @Builder.Default
  private final String minAnalysisFrequency = "";
  @Builder.Default
  private final String maxAnalysisFrequency = "";
  @Builder.Default
  private final String analysisSampleRate = "";
  private final String softwareName;
  private final String softwareVersion;
  private final String protocolReference;
  private final String processingStatement;
  private final String softwareStatement;
  private final String analysisStart;
  private final String analysisEnd;
  private final String clipsStart;
  private final String clipsEnd;
  private final String modelStart;
  private final String modelEnd;
  private final String frequency;
  private final String dataComment;
  

}
