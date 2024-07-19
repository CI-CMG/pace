package edu.colorado.cires.pace.data.object;

import static org.junit.jupiter.api.Assertions.*;

class SoundLevelMetricsPackageTest extends DataQualityTest<SoundLevelMetricsPackage>{

  @Override
  protected SoundLevelMetricsPackage createPackage() {
    return SoundLevelMetricsPackage.builder().build();
  }
}