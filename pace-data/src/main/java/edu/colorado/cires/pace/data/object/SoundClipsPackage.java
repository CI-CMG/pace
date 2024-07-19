package edu.colorado.cires.pace.data.object;

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
public class SoundClipsPackage extends Package implements SoftwareDescription {
private final String softwareNames;
  private final String softwareVersions;
  private final String softwareProtocolCitation;
  private final String softwareDescription;
  private final String softwareProcessingDescription;

  @Override
  public SoundClipsPackage setLocationDetail(LocationDetail locationDetail) {
    return toBuilder().locationDetail(locationDetail).build();
  }

  @Override
  public SoundClipsPackage setProjects(List<String> projects) {
    return toBuilder().projects(projects).build();
  }

  @Override
  public SoundClipsPackage setPlatform(String platform) {
    return toBuilder().platform(platform).build();
  }

  @Override
  public SoundClipsPackage setScientists(List<String> scientists) {
    return toBuilder().scientists(scientists).build();
  }

  @Override
  public SoundClipsPackage setDatasetPackager(String datasetPackager) {
    return toBuilder().datasetPackager(datasetPackager).build();
  }

  @Override
  public SoundClipsPackage setSponsors(List<String> sponsors) {
    return toBuilder().sponsors(sponsors).build();
  }

  @Override
  public SoundClipsPackage setFunders(List<String> funders) {
    return toBuilder().funders(funders).build();
  }

  @Override
  public SoundClipsPackage setInstrument(String instrument) {
    return toBuilder().instrument(instrument).build();
  }

  @Override
  public ObjectWithUniqueField setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }
}
