package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.detectionType.DetectionType;
import edu.colorado.cires.pace.data.object.dataset.detections.DetectionsPackage;
import edu.colorado.cires.pace.data.object.dataset.base.Package;
import edu.colorado.cires.pace.datastore.Datastore;

/**
 * DetectionTypeRepository extends PackageDependencyRepository and holds specifically
 * detection type objects
 */
public class DetectionTypeRepository extends PackageDependencyRepository<DetectionType> {

  /**
   * Creates a detection type repository
   * @param datastore holds detection type objects
   * @param packageDatastore holds package objects
   */
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
