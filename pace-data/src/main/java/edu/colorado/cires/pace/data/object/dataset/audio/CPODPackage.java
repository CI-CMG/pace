package edu.colorado.cires.pace.data.object.dataset.audio;

import edu.colorado.cires.pace.data.object.dataset.audio.metadata.Channel;
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
  public CPODPackage setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  @Override
  public CPODPackage setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}
