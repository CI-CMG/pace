package edu.colorado.cires.pace.data.object;

import edu.colorado.cires.pace.data.object.dataset.soundPropagationModels.SoundPropagationModelsPackage;

public class SoundPropagationModelsTest extends PackageTest<SoundPropagationModelsPackage> {

  @Override
  protected SoundPropagationModelsPackage createObject() {
    return SoundPropagationModelsPackage.builder().build();
  }
}
