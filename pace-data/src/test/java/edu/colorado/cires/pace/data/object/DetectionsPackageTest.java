package edu.colorado.cires.pace.data.object;

import edu.colorado.cires.pace.data.object.dataset.detections.DetectionsPackage;

public class DetectionsPackageTest extends PackageTest<DetectionsPackage> {

  @Override
  protected DetectionsPackage createObject() {
    return DetectionsPackage.builder().build();
  }
}
