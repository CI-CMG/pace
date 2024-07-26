package edu.colorado.cires.pace.data.object;

import edu.colorado.cires.pace.data.object.dataset.soundPropagationModels.SoundPropagationModelsPackage;

class SoundPropagationModelsPackageTest extends PackageTest<SoundPropagationModelsPackage>{

  @Override
  protected SoundPropagationModelsPackage createPackage() {
    return SoundPropagationModelsPackage.builder().build();
  }
}