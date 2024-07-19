package edu.colorado.cires.pace.data.object;

import static org.junit.jupiter.api.Assertions.*;

class CPODPackageTest extends AudioDataTest<CPODPackage> {

  @Override
  protected CPODPackage createPackage() {
    return CPODPackage.builder().build();
  }
}