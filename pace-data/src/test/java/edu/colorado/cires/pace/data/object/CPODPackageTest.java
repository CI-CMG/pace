package edu.colorado.cires.pace.data.object;

import edu.colorado.cires.pace.data.object.dataset.audio.CPODPackage;

class CPODPackageTest extends AudioDataTest<CPODPackage> {

  @Override
  protected CPODPackage createPackage() {
    return CPODPackage.builder().build();
  }
}