package edu.colorado.cires.pace.data.object;

import static org.junit.jupiter.api.Assertions.*;

class DetectionsPackageTest extends DataQualityTest<DetectionsPackage>{

  @Override
  protected DetectionsPackage createPackage() {
    return DetectionsPackage.builder().build();
  }
}