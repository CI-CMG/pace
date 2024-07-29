package edu.colorado.cires.pace.data.object.dataset.audio;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.Channel;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.PackageSensor;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public class DetailedCPODPackage extends DetailedAudioDataPackage {

  @Override
  public DetailedCPODPackage updateChannels(List<Channel<AbstractObject>> channels) {
    return toBuilder().channels(channels).build();
  }

  @Override
  public DetailedCPODPackage updateSensors(List<PackageSensor<AbstractObject>> packageSensors) {
    return toBuilder().sensors(packageSensors).build();
  }

  @Override
  public DetailedCPODPackage setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  @Override
  public DetailedCPODPackage setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }

  @Override
  public DetailedCPODPackage setQualityAnalyst(AbstractObject qualityAnalyst) {
    return toBuilder().qualityAnalyst(qualityAnalyst).build();
  }

}
