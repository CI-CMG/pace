package edu.colorado.cires.pace.data.object;

import edu.colorado.cires.pace.data.object.dataset.audio.AudioPackage;

class AudioPackageTest extends AudioDataTest<AudioPackage>{

  @Override
  protected AudioPackage createPackage() {
    return AudioPackage.builder().build();
  }
}