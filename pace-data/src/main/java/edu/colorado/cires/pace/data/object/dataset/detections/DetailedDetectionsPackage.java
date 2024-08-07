package edu.colorado.cires.pace.data.object.dataset.detections;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.dataset.base.DetailedPackage;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DataQualityEntry;
import jakarta.validation.constraints.NotBlank;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class DetailedDetectionsPackage extends DetailedPackage implements BaseDetectionsPackage<AbstractObject> {

  @NotBlank
  private final AbstractObject soundSource;

  private final AbstractObject qualityAnalyst;
  private final String qualityAnalysisObjectives;
  private final String qualityAnalysisMethod;
  private final String qualityAssessmentDescription;
  @Builder.Default
  private final List<DataQualityEntry> qualityEntries = Collections.emptyList();
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

  @Override
  public DetailedDetectionsPackage setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  @Override
  public DetailedDetectionsPackage setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }

  @Override
  public DetailedDetectionsPackage setSoundSource(AbstractObject soundSource) {
    return toBuilder().soundSource(soundSource).build();
  }

  @Override
  public DetailedDetectionsPackage setQualityAnalyst(AbstractObject qualityAnalyst) {
    return toBuilder().qualityAnalyst(qualityAnalyst).build();
  }
}
