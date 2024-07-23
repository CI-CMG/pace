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
public class CPODPackage extends AudioDataPackage {

  @Override
  public CPODPackage setLocationDetail(LocationDetail locationDetail) {
    return toBuilder().locationDetail(locationDetail).build();
  }

  @Override
  public CPODPackage setProjects(List<String> projects) {
    return toBuilder().projects(projects).build();
  }

  @Override
  public CPODPackage setPlatform(String platform) {
    return toBuilder().platform(platform).build();
  }

  @Override
  public CPODPackage setScientists(List<String> scientists) {
    return toBuilder().scientists(scientists).build();
  }

  @Override
  public CPODPackage setDatasetPackager(String datasetPackager) {
    return toBuilder().datasetPackager(datasetPackager).build();
  }

  @Override
  public CPODPackage updateChannels(List<Channel> channels) {
    return toBuilder().channels(channels).build();
  }

  @Override
  public CPODPackage updateSensors(List<String> sensors) {
    return toBuilder().sensors(sensors).build();
  }

  @Override
  public CPODPackage setQualityAnalyst(String qualityAnalyst) {
    return toBuilder().qualityAnalyst(qualityAnalyst).build();
  }

  @Override
  public CPODPackage setSponsors(List<String> sponsors) {
    return toBuilder().sponsors(sponsors).build();
  }

  @Override
  public CPODPackage setFunders(List<String> funders) {
    return toBuilder().funders(funders).build();
  }

  @Override
  public CPODPackage setInstrument(String instrument) {
    return toBuilder().instrument(instrument).build();
  }

  @Override
  public ObjectWithUniqueField setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  @Override
  public CPODPackage setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}
