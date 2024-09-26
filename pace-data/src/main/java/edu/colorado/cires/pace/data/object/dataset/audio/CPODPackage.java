package edu.colorado.cires.pace.data.object.dataset.audio;

import edu.colorado.cires.pace.data.object.dataset.audio.metadata.Channel;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.PackageSensor;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * CPODPackages contain the same information as an AudioDataPackage in
 * addition to containing a setter for uuid, quality analyst, and visibility
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class CPODPackage extends AudioDataPackage {

  /**
   * Returns a new package with the provided channels
   *
   * @param channels the list of channels to apply
   * @return CPODPackage with the provided channels
   */
  @Override
  public CPODPackage updateChannels(List<Channel<String>> channels) {
    return toBuilder().channels(channels).build();
  }

  /**
   * Returns a new package with the provided sensors
   *
   * @param sensors the list of sensors to apply
   * @return CPODPackage with the provided channels
   */
  @Override
  public CPODPackage updateSensors(List<PackageSensor<String>> sensors) {
    return toBuilder().sensors(sensors).build();
  }

  /**
   * Returns a new package with the provided quality analyst
   *
   * @param qualityAnalyst field for assigning quality analyst to new package
   * @return CPODPackage with the provided quality analyst
   */
  @Override
  public CPODPackage setQualityAnalyst(String qualityAnalyst) {
    return toBuilder().qualityAnalyst(qualityAnalyst).build();
  }

  /**
   * Returns a new package with the provided uuid
   *
   * @param uuid field for assigning uuid to new object
   * @return CPODPackage with the provided uuid
   */
  @Override
  public CPODPackage setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  /**
   * Returns a new package with the provided visibility set
   *
   * @param visible boolean which indicates whether to make the object visible
   *                or invisible
   * @return CPODPackage with provided visibility
   */
  @Override
  public CPODPackage setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}
