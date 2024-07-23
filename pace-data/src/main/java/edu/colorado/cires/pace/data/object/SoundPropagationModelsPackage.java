package edu.colorado.cires.pace.data.object;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class SoundPropagationModelsPackage extends Package implements SoftwareDescription, AudioTimeRange {
  @Positive @NotNull
  private final Float modeledFrequency;
  private final String softwareNames;
  private final String softwareVersions;
  private final String softwareProtocolCitation;
  private final String softwareDescription;
  private final String softwareProcessingDescription;
  private final LocalDateTime audioStartTime;
  private final LocalDateTime audioEndTime;

  @Override
  public SoundPropagationModelsPackage setLocationDetail(LocationDetail locationDetail) {
    return toBuilder().locationDetail(locationDetail).build();
  }

  @Override
  public SoundPropagationModelsPackage setProjects(List<String> projects) {
    return toBuilder().projects(projects).build();
  }

  @Override
  public SoundPropagationModelsPackage setPlatform(String platform) {
    return toBuilder().platform(platform).build();
  }

  @Override
  public SoundPropagationModelsPackage setScientists(List<String> scientists) {
    return toBuilder().scientists(scientists).build();
  }

  @Override
  public SoundPropagationModelsPackage setDatasetPackager(String datasetPackager) {
    return toBuilder().datasetPackager(datasetPackager).build();
  }

  @Override
  public SoundPropagationModelsPackage setSponsors(List<String> sponsors) {
    return toBuilder().sponsors(sponsors).build();
  }

  @Override
  public SoundPropagationModelsPackage setFunders(List<String> funders) {
    return toBuilder().funders(funders).build();
  }

  @Override
  public SoundPropagationModelsPackage setInstrument(String instrument) {
    return toBuilder().instrument(instrument).build();
  }

  @Override
  public ObjectWithUniqueField setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  @Override
  public SoundPropagationModelsPackage setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}
