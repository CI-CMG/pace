package edu.colorado.cires.pace.data.object.dataset.base;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import edu.colorado.cires.pace.data.object.dataset.audio.AudioPackage;
import edu.colorado.cires.pace.data.object.dataset.audio.CPODPackage;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.LocationDetail;
import edu.colorado.cires.pace.data.object.dataset.detections.DetectionsPackage;
import edu.colorado.cires.pace.data.object.dataset.soundClips.SoundClipsPackage;
import edu.colorado.cires.pace.data.object.dataset.soundLevelMetrics.SoundLevelMetricsPackage;
import edu.colorado.cires.pace.data.object.dataset.soundPropagationModels.SoundPropagationModelsPackage;
import edu.colorado.cires.pace.data.validation.ValidPackageIdentifiers;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@JsonTypeInfo(use = Id.NAME, include = As.EXTERNAL_PROPERTY, property = "type")
@JsonSubTypes({
    @Type(value = AudioPackage.class, name = "audio"),
    @Type(value = CPODPackage.class, name = "CPOD"),
    @Type(value = DetectionsPackage.class, name = "detections"),
    @Type(value = SoundClipsPackage.class, name = "sound clips"),
    @Type(value = SoundLevelMetricsPackage.class, name = "sound level metrics"),
    @Type(value = SoundPropagationModelsPackage.class, name = "sound propagation models"),
})
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@ValidPackageIdentifiers
public abstract class Package extends BasePackage<String> {
  @NotBlank
  private final String datasetPackager;
  @NotEmpty @NotNull
  private final List<@NotBlank String> scientists;
  @NotNull
  private final List<@NotBlank String> projects;
  @NotNull @NotEmpty
  private final List<@NotBlank String> sponsors;
  @NotNull @NotEmpty
  private final List<@NotBlank String> funders;
  @NotBlank
  private final String platform;
  @NotBlank
  private final String instrument;

  @Override
  protected List<String> getProjectNames() {
    return this.getProjects() == null ? Collections.emptyList() : this.getProjects();
  }

  public abstract Package setLocationDetail(LocationDetail locationDetail);
  public abstract Package setProjects(List<String> projects);
  public abstract Package setPlatform(String platform);
  public abstract Package setScientists(List<String> scientists);
  public abstract Package setDatasetPackager(String datasetPackager);
  public abstract Package setSponsors(List<String> sponsors);
  public abstract Package setFunders(List<String> funders);
  public abstract Package setInstrument(String instrument);
}
