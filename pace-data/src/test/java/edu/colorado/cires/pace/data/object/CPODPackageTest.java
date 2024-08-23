package edu.colorado.cires.pace.data.object;

import edu.colorado.cires.pace.data.object.dataset.audio.CPODPackage;

public class CPODPackageTest extends PackageTest<CPODPackage> {

  @Override
  protected CPODPackage createObject() {
    return CPODPackage.builder().build();
  }
}
