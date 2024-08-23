package edu.colorado.cires.pace.data.object;

import edu.colorado.cires.pace.data.object.dataset.soundLevelMetrics.SoundLevelMetricsPackage;

public class SoundLevelMetricsPackageTest extends PackageTest<SoundLevelMetricsPackage> {

  @Override
  protected SoundLevelMetricsPackage createObject() {
    return SoundLevelMetricsPackage.builder().build();
  }
}
