package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.DataQuality;
import edu.colorado.cires.pace.data.object.Package;
import edu.colorado.cires.pace.data.object.Person;
import edu.colorado.cires.pace.datastore.Datastore;
import java.util.List;

public class PersonRepository extends PackageDependencyRepository<Person> {


  public PersonRepository(Datastore<Person> datastore, Datastore<Package> dependencyDatastore) {
    super(datastore, true, dependencyDatastore);
  }

  @Override
  protected boolean dependencyAppliesToObject(Package dependency, Person object) {
    String name = object.getName();
    
    boolean applies = dependency.getScientists().contains(name) ||
        dependency.getDatasetPackager().equals(name);
    
    if (dependency instanceof DataQuality dataQuality) {
      applies = applies || dataQuality.getQualityAnalyst().equals(name);
    }
    
    return applies;
  }

  @Override
  protected Package applyObjectToDependentObjects(Person original, Person updated, Package dependency) {
    String originalName = original.getName();
    String newName = updated.getName();
    
    List<String> scientists = replaceStringInList(dependency.getScientists(), originalName, newName);
    String datasetPacker = replaceString(dependency.getDatasetPackager(), originalName, newName);
    
    
    Package updatedPackage = dependency.setScientists(scientists)
        .setDatasetPackager(datasetPacker);
    
    if (updatedPackage instanceof DataQuality dataQuality) {
      updatedPackage = (Package) dataQuality.setQualityAnalyst(
          replaceString(
              dataQuality.getQualityAnalyst(), originalName, newName
          )
      );
    }
    
    return updatedPackage;
  }

}
