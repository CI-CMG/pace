package edu.colorado.cires.pace.data.object.dataset.audio;

import edu.colorado.cires.pace.data.object.dataset.audio.metadata.Channel;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.AudioTimeRange;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.DataQuality;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.PackageSensor;
import java.util.List;

public interface BaseAudioDataPackage<T> extends DataQuality<T>, AudioTimeRange {

  BaseAudioDataPackage<T> updateChannels(List<Channel<T>> channels);

  BaseAudioDataPackage<T> updateSensors(List<PackageSensor<T>> sensors);

  String getInstrumentId();

  java.time.LocalDateTime getDeploymentTime();

  java.time.LocalDateTime getRecoveryTime();

  String getComments();

  List<PackageSensor<T>> getSensors();

  List<Channel<T>> getChannels();

  Float getHydrophoneSensitivity();

  String getFrequencyRange();

  Float getGain();
}
