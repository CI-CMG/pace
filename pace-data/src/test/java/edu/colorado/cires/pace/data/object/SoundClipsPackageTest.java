package edu.colorado.cires.pace.data.object;

import static org.junit.jupiter.api.Assertions.*;

class SoundClipsPackageTest extends PackageTest<SoundClipsPackage>{

  @Override
  protected SoundClipsPackage createPackage() {
    return SoundClipsPackage.builder().build();
  }
}