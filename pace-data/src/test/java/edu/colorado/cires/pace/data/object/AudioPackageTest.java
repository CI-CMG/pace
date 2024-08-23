package edu.colorado.cires.pace.data.object;

import edu.colorado.cires.pace.data.object.dataset.audio.AudioPackage;

public class AudioPackageTest extends PackageTest<AudioPackage> {

  @Override
  protected AudioPackage createObject() {
    return AudioPackage.builder().build();
  }
}
