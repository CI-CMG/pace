package edu.colorado.cires.pace.packaging;

import edu.colorado.cires.pace.data.object.dataset.base.Package;
import java.nio.file.Path;
import java.util.List;

public class ProcessSet {
  public final List<Package> processedPackages;
  public final List<Package> zeroBytePackages;
  public final List<List<Path>> zeroByteLists;

  public ProcessSet(List<Package> processedPackages, List<Package> zeroBytePackages, List<List<Path>> zeroByteLists) {
    this.processedPackages = processedPackages;
    this.zeroBytePackages = zeroBytePackages;
    this.zeroByteLists = zeroByteLists;
  }
}
