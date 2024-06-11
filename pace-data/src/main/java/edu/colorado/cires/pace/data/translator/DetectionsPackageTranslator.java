package edu.colorado.cires.pace.data.translator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder(toBuilder = true)
@Jacksonized
public class DetectionsPackageTranslator extends PackageTranslator {
  
  private final String soundSource;
  
  private final QualityControlDetailTranslator qualityControlDetailTranslator;
  
  private final String softwareNames;
  private final String softwareVersions;
  private final String softwareProtocolCitation;
  private final String softwareDescription;
  private final String softwareProcessingDescription;
  private final String analysisTimeZone;
  private final String analysisEffort;
  private final String sampleRate;
  private final String minFrequency;
  private final String maxFrequency;

}
