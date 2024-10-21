package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.DataQuality;
import edu.colorado.cires.pace.data.object.dataset.base.Package;
import edu.colorado.cires.pace.data.object.contact.person.Person;
import edu.colorado.cires.pace.datastore.Datastore;
import java.util.List;

/**
 * PersonRepository extends PackageDependencyRepository and holds specifically
 * person objects
 */
public class PersonRepository extends PackageDependencyRepository<Person> {

  /**
   * Creates a person repository
   * @param datastore holds person objects
   * @param dependencyDatastore holds package objects
   */
  public PersonRepository(Datastore<Person> datastore, Datastore<Package> dependencyDatastore) {
    super(datastore, true, dependencyDatastore);
  }

  @Override
  protected boolean dependencyAppliesToObject(Package dependency, Person object) {
    String name = object.getName();
    
    boolean applies = dependency.getScientists().contains(name) ||
        dependency.getDatasetPackager().equals(name);
    
    if (dependency instanceof DataQuality<?> dataQuality) {
      String analyst = (String) dataQuality.getQualityAnalyst(); 
      applies = applies || analyst.equals(name);
    }
    
    return applies;
  }

  @Override
  protected Package applyObjectToDependentObjects(Person original, Person updated, Package dependency) {
    String originalName = original.getName();
    String newName = updated.getName();
    
    List<String> scientists = replaceStringInList(dependency.getScientists(), originalName, newName);
    String datasetPacker = replaceString(dependency.getDatasetPackager(), originalName, newName);
    
    
    Package updatedPackage = dependency.toBuilder()
        .scientists(scientists)
        .datasetPackager(datasetPacker)
        .build();
    
    if (updatedPackage instanceof DataQuality<?> dataQuality) {
      DataQuality<String> quality = (DataQuality<String>) dataQuality; 
      updatedPackage = (Package) quality.setQualityAnalyst(
          replaceString(
              quality.getQualityAnalyst(), originalName, newName
          )
      );
    }
    
    return updatedPackage;
  }

}
