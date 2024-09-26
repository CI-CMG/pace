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
 * AudioPackage contains the same information as an AudioDataPackage in
 * addition to containing a setter for uuid and visibility
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class AudioPackage extends AudioDataPackage {

  /**
   * Returns a new AudioPackage with the provided channels
   *
   * @param channels the updated list of channels that applies to the package
   * @return the package with the updated list of channels
   */
  @Override
  public AudioPackage updateChannels(List<Channel<String>> channels) {
    return toBuilder().channels(channels).build();
  }

  /**
   * Returns a new AudioPackage with the provided list of sensors
   *
   * @param sensors the updated list of sensors that applies to the package
   * @return the package with the updated list of sensors
   */
  @Override
  public AudioPackage updateSensors(List<PackageSensor<String>> sensors) {
    return toBuilder().sensors(sensors).build();
  }

  /**
   * Sets the uuid of the returned package to the provided value
   *
   * @param uuid field for assigning uuid to new object
   * @return new AudioPackage with the provided uuid
   */
  @Override
  public AudioPackage setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  /**
   * Sets the visibility of the returned package to the provided value
   *
   * @param visible boolean which indicates whether to make the object visible
   *                or invisible
   * @return new AudioPackage with the provided visibility
   */
  @Override
  public AudioPackage setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}
