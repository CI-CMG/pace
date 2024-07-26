package edu.colorado.cires.pace.data.object;

import edu.colorado.cires.pace.data.object.dataset.soundClips.SoundClipsPackage;

class SoundClipsPackageTest extends PackageTest<SoundClipsPackage>{

  @Override
  protected SoundClipsPackage createPackage() {
    return SoundClipsPackage.builder().build();
  }
}