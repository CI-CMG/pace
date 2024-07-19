package edu.colorado.cires.pace.data.object;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.cires.pace.data.ObjectWithUniqueFieldTest;
import java.util.List;
import org.junit.jupiter.api.Test;

abstract class PackageTest<P extends Package> extends ObjectWithUniqueFieldTest<P> {
  
  protected abstract P createPackage();

  @Override
  protected P createObject() {
    return createPackage();
  }

  @Test
  void setLocationDetail() {
    LocationDetail locationDetail = MobileMarineLocation.builder()
        .seaArea("sea-area")
        .locationDerivationDescription("derivation")
        .vessel("vessel")
        .build();
    
    P p = createPackage();
    assertNull(p.getLocationDetail());
    p = (P) p.setLocationDetail(locationDetail);
    assertEquals(locationDetail, p.getLocationDetail());
  }

  @Test
  void setProjects() {
    List<String> projects = List.of("1", "2");
    P p = createPackage();
    assertNull(p.getProjects());
    p = (P) p.setProjects(projects);
    assertEquals(projects, p.getProjects());
  }

  @Test
  void setPlatform() {
    String platform = "platform";
    P p = createPackage();
    assertNull(p.getPlatform());
    p = (P) p.setPlatform(platform);
    assertEquals(platform, p.getPlatform());
  }

  @Test
  void setScientists() {
    List<String> scientists = List.of("1", "2");
    P p = createPackage();
    assertNull(p.getScientists());
    p = (P) p.setScientists(scientists);
    assertEquals(scientists, p.getScientists());
  }

  @Test
  void setDatasetPackager() {
    String packager = "packager";
    P p = createPackage();
    assertNull(p.getDatasetPackager());
    p = (P) p.setDatasetPackager(packager);
    assertEquals(packager, p.getDatasetPackager());
  }

  @Test
  void setSponsors() {
    List<String> sponsors = List.of("1", "2");
    P p = createPackage();
    assertNull(p.getSponsors());
    p = (P) p.setSponsors(sponsors);
    assertEquals(sponsors, p.getSponsors());
  }

  @Test
  void setFunders() {
    List<String> funders = List.of("1", "2");
    P p = createPackage();
    assertNull(p.getFunders());
    p = (P) p.setFunders(funders);
    assertEquals(funders, p.getFunders());
  }

  @Test
  void setInstrument() {
    String instrument = "instrument";
    P p = createPackage();
    assertNull(p.getInstrument());
    p = (P) p.setInstrument(instrument);
    assertEquals(instrument, p.getInstrument());
  }
}