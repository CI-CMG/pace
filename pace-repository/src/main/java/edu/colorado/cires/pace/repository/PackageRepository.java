package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.dataset.audio.AudioDataPackage;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.Channel;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.DataQuality;
import edu.colorado.cires.pace.data.object.detectionType.DetectionType;
import edu.colorado.cires.pace.data.object.dataset.detections.DetectionsPackage;
import edu.colorado.cires.pace.data.object.instrument.Instrument;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.LocationDetail;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.MarineLocation;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.MobileMarineLocation;
import edu.colorado.cires.pace.data.object.contact.organization.Organization;
import edu.colorado.cires.pace.data.object.dataset.base.Package;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.PackageSensor;
import edu.colorado.cires.pace.data.object.contact.person.Person;
import edu.colorado.cires.pace.data.object.platform.Platform;
import edu.colorado.cires.pace.data.object.project.Project;
import edu.colorado.cires.pace.data.object.sea.Sea;
import edu.colorado.cires.pace.data.object.sensor.base.Sensor;
import edu.colorado.cires.pace.data.object.ship.Ship;
import edu.colorado.cires.pace.datastore.Datastore;
import edu.colorado.cires.pace.datastore.DatastoreException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;

/**
 * PackageRepository extends CRUDRepository and specifically holds
 * package objects
 */
public class PackageRepository extends CRUDRepository<Package> implements DownstreamDependencyRepository<Package> {
  
  private final Datastore<DetectionType> detectionTypeDatastore;
  private final Datastore<Instrument> instrumentDatastore;
  private final Datastore<Organization> organizationDatastore;
  private final Datastore<Person> personDatastore;
  private final Datastore<Platform> platformDatastore;
  private final Datastore<Project> projectDatastore;
  private final Datastore<Sea> seaDatastore;
  private final Datastore<Sensor> sensorDatastore;
  private final Datastore<Ship> shipDatastore;

  /**
   * Creates a package repository
   * @param datastore holds package objects
   * @param detectionTypeDatastore holds detection type objects
   * @param instrumentDatastore holds instrument objects
   * @param organizationDatastore holds organization objects
   * @param personDatastore holds person objects
   * @param platformDatastore holds platform objects
   * @param projectDatastore holds project objects
   * @param seaDatastore holds sea area objects
   * @param sensorDatastore holds sensor objects
   * @param shipDatastore holds ship objects
   */
  public PackageRepository(Datastore<Package> datastore, Datastore<DetectionType> detectionTypeDatastore, Datastore<Instrument> instrumentDatastore,
      Datastore<Organization> organizationDatastore, Datastore<Person> personDatastore, Datastore<Platform> platformDatastore,
      Datastore<Project> projectDatastore, Datastore<Sea> seaDatastore, Datastore<Sensor> sensorDatastore, Datastore<Ship> shipDatastore) {
    super(datastore);
    this.detectionTypeDatastore = detectionTypeDatastore;
    this.instrumentDatastore = instrumentDatastore;
    this.organizationDatastore = organizationDatastore;
    this.personDatastore = personDatastore;
    this.platformDatastore = platformDatastore;
    this.projectDatastore = projectDatastore;
    this.seaDatastore = seaDatastore;
    this.sensorDatastore = sensorDatastore;
    this.shipDatastore = shipDatastore;
  }

  /**
   * Saves a package to the datastore
   * @param object object to be saved to the repository
   * @return Package object after any changes
   * @throws DatastoreException thrown in case of error interacting with datastore
   * @throws ConflictException thrown in case of duplicate uuid in datastore
   * @throws NotFoundException thrown in case of object not found
   * @throws BadArgumentException thrown in case of bad argument
   */
  @Override
  public Package create(Package object) throws DatastoreException, ConflictException, NotFoundException, BadArgumentException {
    checkDownstreamDependencies(object);
    return super.create(object);
  }

  /**
   * Updates a package in the datastore with the provided uuid to the provided package
   * @param uuid uuid to identify object to update by
   * @param object updated object
   * @return Package updated package
   * @throws DatastoreException thrown in case of error interacting with datastore
   * @throws ConflictException thrown in case of duplicate uuid in datastore
   * @throws NotFoundException thrown in case of object not found
   * @throws BadArgumentException thrown in case of bad argument
   */
  @Override
  public Package update(UUID uuid, Package object) throws DatastoreException, ConflictException, NotFoundException, BadArgumentException {
    checkDownstreamDependencies(object);
    return super.update(uuid, object);
  }

  /**
   * Checks the downstream dependencies of a package object
   * @param object package object to check the dependencies of
   * @throws DatastoreException thrown in case of error interacting with datastore
   */
  @Override
  public void checkDownstreamDependencies(Package object) throws DatastoreException {
    validate(object);
    
    Set<ConstraintViolation<Package>> constraintViolations = new HashSet<>(0);
    
    checkDependency(object.getInstrument(), instrumentDatastore, "instrument", constraintViolations);
    checkDependency(object.getPlatform(), platformDatastore, "platform", constraintViolations);
    checkDependencies(object.getProjects(), projectDatastore, "projects", constraintViolations);
    checkDependencies(object.getScientists(), personDatastore, "scientists", constraintViolations);
    if (!(object.getDatasetPackager() == null)) {
      checkDependency(object.getDatasetPackager(), personDatastore, "datasetPackager", constraintViolations);
    }
    checkDependencies(object.getFunders(), organizationDatastore, "funders", constraintViolations);
    checkDependencies(object.getSponsors(), organizationDatastore, "sponsors", constraintViolations);

    LocationDetail locationDetail = object.getLocationDetail();
    if (locationDetail instanceof MobileMarineLocation mobileMarineLocation) {
      checkDependency(mobileMarineLocation.getVessel(), shipDatastore, "locationDetail.vessel", constraintViolations);
    } 
    
    if (locationDetail instanceof MarineLocation marineLocation) {
      checkDependency(marineLocation.getSeaArea(), seaDatastore, "locationDetail.seaArea", constraintViolations);
    }

    if (object instanceof DetectionsPackage detectionsPackage) {
      checkDependency(detectionsPackage.getSoundSource(), detectionTypeDatastore, "soundSource", constraintViolations);
    } else if (object instanceof AudioDataPackage audioDataPackage) {
      checkDependencies(audioDataPackage.getSensors().stream().map(PackageSensor::getSensor).toList(), sensorDatastore, "sensors", constraintViolations);
//      List<Channel<String>> channels = audioDataPackage.getChannels();
//      for (int i = 0; i < channels.size(); i++) {
//        checkDependency(channels.get(i).getSensor().getSensor(), sensorDatastore, String.format("channels[%s].sensor", i), constraintViolations);
//      }
    }
    
    if (object instanceof DataQuality<?> dataQuality) {
      String analyst = (String) dataQuality.getQualityAnalyst();
      if (StringUtils.isNotBlank(analyst)) {
        checkDependency(analyst, personDatastore, "qualityAnalyst", constraintViolations);
      }
    }
    
    if (!constraintViolations.isEmpty()) {
      throw new ConstraintViolationException(String.format(
          "%s validation failed", getClassName()
      ), constraintViolations);
    }
    
  }
  
  private <T extends AbstractObject> void checkDependencies(List<String> objectUniqueFields, Datastore<T> datastore, String rootPath, Set<ConstraintViolation<Package>> constraintViolations)
      throws DatastoreException {
    for (int i = 0; i < objectUniqueFields.size(); i++) {
      checkDependency(
          objectUniqueFields.get(i),
          datastore,
          String.format("%s[%s]", rootPath, i),
          constraintViolations
      );
    }
  }
  
  private <T extends AbstractObject> void checkDependency(String objectUniqueField, Datastore<T> datastore, String path, Set<ConstraintViolation<Package>> constraintViolations)
      throws DatastoreException {
    Optional<T> maybeObject = datastore.findByUniqueField(objectUniqueField);
    if (maybeObject.isEmpty()) {
      constraintViolations.add(ConstraintViolationFactory.create(
          String.format(
              "%s with %s = %s does not exist",
              datastore.getClassName(),
              datastore.getUniqueFieldName(),
              objectUniqueField
          ),
          path
      ));
    }
  }
  
  
}
