package edu.colorado.cires.pace.data.object.dataset.audio;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.Channel;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.LocationDetail;
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
public class CPODPackage extends AudioDataPackage {

  @Override
  public CPODPackage setLocationDetail(LocationDetail locationDetail) {
    return toBuilder().locationDetail(locationDetail).build();
  }

  @Override
  public CPODPackage setProjectName(List<String> projectName) {
    return toBuilder().projectName(projectName).build();
  }

  @Override
  public CPODPackage setPlatformName(String platformName) {
    return toBuilder().platformName(platformName).build();
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
  public CPODPackage updateChannels(List<Channel<String>> channels) {
    return toBuilder().channels(channels).build();
  }

  @Override
  public CPODPackage updateSensors(List<PackageSensor<String>> sensors) {
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
  public CPODPackage setInstrumentType(String instrumentType) {
    return toBuilder().instrumentType(instrumentType).build();
  }

  @Override
  public AbstractObject setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  @Override
  public CPODPackage setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}
