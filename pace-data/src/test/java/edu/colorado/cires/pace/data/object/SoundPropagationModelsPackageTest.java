package edu.colorado.cires.pace.data.object;

import static org.junit.jupiter.api.Assertions.*;

class SoundPropagationModelsPackageTest extends PackageTest<SoundPropagationModelsPackage>{

  @Override
  protected SoundPropagationModelsPackage createPackage() {
    return SoundPropagationModelsPackage.builder().build();
  }
}