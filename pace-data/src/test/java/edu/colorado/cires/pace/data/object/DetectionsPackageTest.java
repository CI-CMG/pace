package edu.colorado.cires.pace.data.object;

import edu.colorado.cires.pace.data.object.dataset.detections.DetectionsPackage;

class DetectionsPackageTest extends DataQualityTest<DetectionsPackage>{

  @Override
  protected DetectionsPackage createPackage() {
    return DetectionsPackage.builder().build();
  }
}