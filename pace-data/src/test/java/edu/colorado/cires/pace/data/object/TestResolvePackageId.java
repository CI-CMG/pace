package edu.colorado.cires.pace.data.object;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.UUID;
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
    Dataset dataset = new Dataset(
        AudioPackage.builder()
            .siteOrCruiseName(siteOrCruiseName)
            .deploymentId(deploymentId)
            .projects(Collections.singletonList(projectName))
    ) {
      @Override
      public ObjectWithUniqueField setUuid(UUID uuid) {
        return null;
      }
    };
    
    assertEquals(expectedValue, dataset.getPackageId());
  }

  @Test
  void testResolvePackageIdEmptyProjects() {
    Dataset dataset = new Dataset(
        AudioPackage.builder()
            .siteOrCruiseName("socName")
            .deploymentId("dId")
    ) {
      @Override
      public ObjectWithUniqueField setUuid(UUID uuid) {
        return null;
      }
    };

    assertEquals(String.format(
        "%s_%s", dataset.getSiteOrCruiseName(), dataset.getDeploymentId()
    ), dataset.getPackageId());
  }

}
