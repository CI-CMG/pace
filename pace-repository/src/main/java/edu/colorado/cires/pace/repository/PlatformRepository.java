package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.AudioPackage;
import edu.colorado.cires.pace.data.object.CPODPackage;
import edu.colorado.cires.pace.data.object.DetectionsPackage;
import edu.colorado.cires.pace.data.object.Package;
import edu.colorado.cires.pace.data.object.Platform;
import edu.colorado.cires.pace.data.object.SoundClipsPackage;
import edu.colorado.cires.pace.data.object.SoundLevelMetricsPackage;
import edu.colorado.cires.pace.data.object.SoundPropagationModelsPackage;
import edu.colorado.cires.pace.datastore.Datastore;
import java.util.UUID;

public class PlatformRepository extends PackageDependencyRepository<Platform> {

  public PlatformRepository(Datastore<Platform> datastore, Datastore<Package> packageDatastore) {
    super(datastore, packageDatastore);
  }

  @Override
  protected boolean dependencyAppliesToObject(Package dependency, Platform object) {
    return dependency.getPlatform().equals(object.getName());
  }

  @Override
  protected Package applyObjectToDependentObjects(Platform original, Platform updated, Package dependency) {
    return dependency.setPlatform(
        replaceString(
            dependency.getPlatform(), original.getName(), updated.getName()
        )
    );
  }
}
