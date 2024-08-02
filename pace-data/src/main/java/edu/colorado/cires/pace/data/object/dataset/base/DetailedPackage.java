package edu.colorado.cires.pace.data.object.dataset.base;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.base.AbstractObjectWithName;
import edu.colorado.cires.pace.data.object.dataset.audio.DetailedAudioPackage;
import edu.colorado.cires.pace.data.object.dataset.audio.DetailedCPODPackage;
import edu.colorado.cires.pace.data.object.dataset.detections.DetailedDetectionsPackage;
import edu.colorado.cires.pace.data.object.dataset.soundClips.DetailedSoundClipsPackage;
import edu.colorado.cires.pace.data.object.dataset.soundLevelMetrics.DetailedSoundLevelMetricsPackage;
import edu.colorado.cires.pace.data.object.dataset.soundPropagationModels.DetailedSoundPropagationModelsPackage;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@JsonTypeInfo(use = Id.NAME, include = As.EXTERNAL_PROPERTY, property = "type")
@JsonSubTypes({
    @Type(value = DetailedAudioPackage.class, name = "audio"),
    @Type(value = DetailedCPODPackage.class, name = "CPOD"),
    @Type(value = DetailedDetectionsPackage.class, name = "detections"),
    @Type(value = DetailedSoundClipsPackage.class, name = "sound clips"),
    @Type(value = DetailedSoundLevelMetricsPackage.class, name = "sound level metrics"),
    @Type(value = DetailedSoundPropagationModelsPackage.class, name = "sound propagation models"),
})
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public abstract class DetailedPackage extends BasePackage<Object> {
  private final Object datasetPackager;
  private final List<Object> scientists;
  private final List<Object> projectName;
  private final List<Object> sponsors;
  private final List<Object> funders;
  private final String platformName;
  private final Object instrumentType;

  @Override
  protected List<String> getProjectNames() {
    return getProjectName().stream()
            .map(o -> (String) o)
            .toList();
  }
}
