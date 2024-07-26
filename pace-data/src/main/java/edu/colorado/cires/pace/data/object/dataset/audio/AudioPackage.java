package edu.colorado.cires.pace.data.object.dataset.audio;

import edu.colorado.cires.pace.data.object.dataset.audio.metadata.Channel;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.LocationDetail;
import edu.colorado.cires.pace.data.object.base.ObjectWithUniqueField;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.PackageSensor;
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
public class AudioPackage extends AudioDataPackage {

  @Override
  public AudioPackage setScientists(List<String> scientists) {
    return toBuilder().scientists(scientists).build();
  }

  @Override
  public AudioPackage setDatasetPackager(String datasetPackager) {
    return toBuilder().datasetPackager(datasetPackager).build();
  }

  @Override
  public AudioPackage setSponsors(List<String> sponsors) {
    return toBuilder().sponsors(sponsors).build();
  }

  @Override
  public AudioPackage setFunders(List<String> funders) {
    return toBuilder().funders(funders).build();
  }

  @Override
  public AudioPackage setQualityAnalyst(String qualityAnalyst) {
    return toBuilder().qualityAnalyst(qualityAnalyst).build();
  }

  @Override
  public AudioPackage setLocationDetail(LocationDetail locationDetail) {
    return toBuilder().locationDetail(locationDetail).build();
  }

  @Override
  public AudioPackage setProjects(List<String> projects) {
    return toBuilder().projects(projects).build();
  }

  @Override
  public AudioPackage setPlatform(String platform) {
    return toBuilder().platform(platform).build();
  }

  @Override
  public AudioPackage updateChannels(List<Channel> channels) {
    return toBuilder().channels(channels).build();
  }

  @Override
  public AudioPackage updateSensors(List<PackageSensor> sensors) {
    return toBuilder().sensors(sensors).build();
  }

  @Override
  public AudioPackage setInstrument(String instrument) {
    return toBuilder().instrument(instrument).build();
  }

  @Override
  public ObjectWithUniqueField setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  @Override
  public AudioPackage setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}
