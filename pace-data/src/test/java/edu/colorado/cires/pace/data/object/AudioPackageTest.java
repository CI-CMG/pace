package edu.colorado.cires.pace.data.object;

import static org.junit.jupiter.api.Assertions.*;

class AudioPackageTest extends AudioDataTest<AudioPackage>{

  @Override
  protected AudioPackage createPackage() {
    return AudioPackage.builder().build();
  }
}