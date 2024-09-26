package edu.colorado.cires.pace.data.object.dataset.audio;

import edu.colorado.cires.pace.data.object.dataset.audio.metadata.Channel;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.AudioTimeRange;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.DataQuality;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.PackageSensor;
import java.util.List;

/**
 * BaseAudioDataPackage provides the getters and update functions
 * for other package types to inherit
 */
public interface BaseAudioDataPackage<T> extends DataQuality<T>, AudioTimeRange {

  /**
   * Returns a BaseAudioDataPackage with the provided channels
   *
   * @param channels the list of channels to apply
   * @return BaseAudioDataPackage with the provided channels
   */
  BaseAudioDataPackage<T> updateChannels(List<Channel<T>> channels);

  /**
   * Returns a BaseAudioDataPackage with the provided sensors
   *
   * @param sensors the list of sensors to apply
   * @return BaseAudioDataPackage with the provided sensors
   */
  BaseAudioDataPackage<T> updateSensors(List<PackageSensor<T>> sensors);

  /**
   * Returns the package's instrument ID
   *
   * @return String of package's instrument ID
   */
  String getInstrumentId();

  /**
   * Returns the deployment time of the package
   *
   * @return LocalDateTime deployment time of package
   */
  java.time.LocalDateTime getDeploymentTime();

  /**
   * Returns the recovery time of the package
   *
   * @return LocalDateTime recovery time of package
   */
  java.time.LocalDateTime getRecoveryTime();

  /**
   * Returns the comments of the package
   *
   * @return String comments of the package
   */
  String getComments();

  /**
   * Returns the sensors of the package
   *
   * @return List of sensors of the package
   */
  List<PackageSensor<T>> getSensors();

  /**
   * Returns the channels of the package
   *
   * @return List of channels of the package
   */
  List<Channel<T>> getChannels();
}
