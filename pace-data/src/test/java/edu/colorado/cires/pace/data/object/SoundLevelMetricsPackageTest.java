package edu.colorado.cires.pace.data.object;

import edu.colorado.cires.pace.data.object.dataset.soundLevelMetrics.SoundLevelMetricsPackage;

class SoundLevelMetricsPackageTest extends DataQualityTest<SoundLevelMetricsPackage>{

  @Override
  protected SoundLevelMetricsPackage createPackage() {
    return SoundLevelMetricsPackage.builder().build();
  }
}