package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.DetectionType;
import edu.colorado.cires.pace.data.object.DetectionsPackage;
import edu.colorado.cires.pace.data.object.Package;
import edu.colorado.cires.pace.datastore.Datastore;

public class DetectionTypeRepository extends PackageDependencyRepository<DetectionType> {

  public DetectionTypeRepository(Datastore<DetectionType> datastore, Datastore<Package> packageDatastore) {
    super(datastore, packageDatastore);
  }

  @Override
  protected boolean dependencyAppliesToObject(Package dependency, DetectionType object) {
    if (dependency instanceof DetectionsPackage detectionsPackage) {
      return detectionsPackage.getSoundSource().equals(object.getSource());
    }
    return false;
  }

  @Override
  protected Package applyObjectToDependentObjects(DetectionType original, DetectionType updated, Package dependency) {
    DetectionsPackage detectionsPackage = (DetectionsPackage) dependency;
    return detectionsPackage.setSoundSource(
        replaceString(
            detectionsPackage.getSoundSource(), original.getSource(), updated.getSource()
        )
    );
  }
}
