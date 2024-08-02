package edu.colorado.cires.pace.data.object;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.object.dataset.audio.AudioPackage;
import edu.colorado.cires.pace.data.object.dataset.base.Package;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class TestResolvePackageId {
  
  @ParameterizedTest
  @CsvSource(value = {
      "socName,dId,pn,pn_socName_dId",
      "socName,,,socName",
      "socName,dId,,socName_dId",
      "socName,,pn,pn_socName",
      ",dId,,dId",
      "socName,dId,,socName_dId",
      ",dId,pn,pn_dId",
      ",,pn,pn",
      "socName,,pn,pn_socName",
      ",dId,pn,pn_dId",
      ",,,"
  })
  void testResolvePackageId(String siteOrCruiseName, String deploymentId, String projectName, String expectedValue) {
    Package aPackage = AudioPackage.builder()
        .site(siteOrCruiseName)
        .deploymentId(deploymentId)
        .projectName(Collections.singletonList(projectName))
        .build();
    
    assertEquals(expectedValue, aPackage.getPackageId());
  }

  @Test
  void testResolvePackageIdEmptyProjects() {
    Package aPackage = AudioPackage.builder()
        .site("socName")
        .deploymentId("dId").build();

    assertEquals(String.format(
        "%s_%s", aPackage.getSite(), aPackage.getDeploymentId()
    ), aPackage.getPackageId());
  }

}
