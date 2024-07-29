package edu.colorado.cires.pace.data.object.dataset.audio;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.Channel;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.PackageSensor;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.LocationDetail;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public class DetailedAudioPackage extends DetailedAudioDataPackage {

  @Override
  public DetailedAudioPackage updateChannels(List<Channel<AbstractObject>> channels) {
    return toBuilder().channels(channels).build();
  }

  @Override
  public DetailedAudioPackage updateSensors(List<PackageSensor<AbstractObject>> packageSensors) {
    return toBuilder().sensors(packageSensors).build();
  }

  @Override
  public DetailedAudioPackage setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  @Override
  public DetailedAudioPackage setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }

  @Override
  public DetailedAudioPackage setQualityAnalyst(AbstractObject qualityAnalyst) {
    return toBuilder().qualityAnalyst(qualityAnalyst).build();
  }
}
