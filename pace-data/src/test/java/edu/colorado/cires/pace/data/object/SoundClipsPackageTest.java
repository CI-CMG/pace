package edu.colorado.cires.pace.data.object;

import edu.colorado.cires.pace.data.object.dataset.soundClips.SoundClipsPackage;

public class SoundClipsPackageTest extends PackageTest<SoundClipsPackage> {

  @Override
  protected SoundClipsPackage createObject() {
    return SoundClipsPackage.builder().build();
  }
}
