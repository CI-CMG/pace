package edu.colorado.cires.pace.data.object.dataset.soundClips;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.dataset.base.Package;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.LocationDetail;
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
public class SoundClipsPackage extends Package implements BaseSoundClipsPackage {
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
  public SoundClipsPackage setProjectName(List<String> projectName) {
    return toBuilder().projectName(projectName).build();
  }

  @Override
  public SoundClipsPackage setPlatformName(String platformName) {
    return toBuilder().platformName(platformName).build();
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
  public SoundClipsPackage setInstrumentType(String instrumentType) {
    return toBuilder().instrumentType(instrumentType).build();
  }

  @Override
  public AbstractObject setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  @Override
  public SoundClipsPackage setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}
