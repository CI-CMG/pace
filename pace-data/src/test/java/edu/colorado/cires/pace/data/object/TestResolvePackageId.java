package edu.colorado.cires.pace.data.object;

import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
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
    PackageInfo packageInfo = new PackageInfo() {
      @Override
      public String getSiteOrCruiseName() {
        return siteOrCruiseName;
      }

      @Override
      public String getDeploymentId() {
        return deploymentId;
      }

      @Override
      public Person getDatasetPackager() {
        return null;
      }

      @Override
      public List<@Valid Project> getProjects() {
        return List.of(Project.builder()
                .name(projectName)
            .build());
      }

      @Override
      public LocalDate getPublicReleaseDate() {
        return null;
      }
    };
    
    assertEquals(expectedValue, packageInfo.getPackageId());
  }

  @Test
  void testResolvePackageIdEmptyProjects() {
    PackageInfo packageInfo = new PackageInfo() {
      @Override
      public String getSiteOrCruiseName() {
        return "socName";
      }

      @Override
      public String getDeploymentId() {
        return "dId";
      }

      @Override
      public Person getDatasetPackager() {
        return null;
      }

      @Override
      public List<@Valid Project> getProjects() {
        return Collections.emptyList();
      }

      @Override
      public LocalDate getPublicReleaseDate() {
        return null;
      }
    };

    assertEquals(String.format(
        "%s_%s", packageInfo.getSiteOrCruiseName(), packageInfo.getDeploymentId()
    ), packageInfo.getPackageId());
  }

}
