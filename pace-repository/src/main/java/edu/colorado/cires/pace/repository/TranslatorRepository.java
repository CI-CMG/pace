package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.translator.AudioPackageTranslator;
import edu.colorado.cires.pace.data.translator.AudioSensorTranslator;
import edu.colorado.cires.pace.data.translator.CPODPackageTranslator;
import edu.colorado.cires.pace.data.translator.DepthSensorTranslator;
import edu.colorado.cires.pace.data.translator.DetectionTypeTranslator;
import edu.colorado.cires.pace.data.translator.DetectionsPackageTranslator;
import edu.colorado.cires.pace.data.translator.FileTypeTranslator;
import edu.colorado.cires.pace.data.translator.InstrumentTranslator;
import edu.colorado.cires.pace.data.translator.OrganizationTranslator;
import edu.colorado.cires.pace.data.translator.OtherSensorTranslator;
import edu.colorado.cires.pace.data.translator.PersonTranslator;
import edu.colorado.cires.pace.data.translator.PlatformTranslator;
import edu.colorado.cires.pace.data.translator.ProjectTranslator;
import edu.colorado.cires.pace.data.translator.SeaTranslator;
import edu.colorado.cires.pace.data.translator.ShipTranslator;
import edu.colorado.cires.pace.data.translator.SoundClipsPackageTranslator;
import edu.colorado.cires.pace.data.translator.SoundLevelMetricsPackageTranslator;
import edu.colorado.cires.pace.data.translator.SoundPropagationModelsPackageTranslator;
import edu.colorado.cires.pace.data.translator.Translator;
import edu.colorado.cires.pace.datastore.Datastore;
import java.util.UUID;

public class TranslatorRepository extends CRUDRepository<Translator> {

  public TranslatorRepository(Datastore<Translator> datastore) {
    super(datastore);
  }

  @Override
  protected Translator setUUID(Translator object, UUID uuid) throws BadArgumentException {
    if (object instanceof ShipTranslator shipTranslator) {
      return shipTranslator.toBuilder()
          .uuid(uuid)
          .build();
    } else if (object instanceof SoundClipsPackageTranslator soundClipsPackageTranslator) {
      return soundClipsPackageTranslator.toBuilder()
          .uuid(uuid)
          .build();
    } else if (object instanceof SoundLevelMetricsPackageTranslator soundLevelMetricsPackageTranslator) {
      return soundLevelMetricsPackageTranslator.toBuilder()
          .uuid(uuid)
          .build();
    } else if (object instanceof SoundPropagationModelsPackageTranslator soundPropagationModelsPackageTranslator) {
      return soundPropagationModelsPackageTranslator.toBuilder()
          .uuid(uuid)
          .build();
    } else if (object instanceof AudioPackageTranslator audioPackageTranslator) {
      return audioPackageTranslator.toBuilder()
          .uuid(uuid)
          .build();
    } else if (object instanceof AudioSensorTranslator audioSensorTranslator) {
      return audioSensorTranslator.toBuilder()
          .uuid(uuid)
          .build();
    } else if (object instanceof CPODPackageTranslator cpodPackageTranslator) {
      return cpodPackageTranslator.toBuilder()
          .uuid(uuid)
          .build();
    } else if (object instanceof DepthSensorTranslator depthSensorTranslator) {
      return depthSensorTranslator.toBuilder()
          .uuid(uuid)
          .build();
    } else if (object instanceof DetectionTypeTranslator detectionTypeTranslator) {
      return detectionTypeTranslator.toBuilder()
          .uuid(uuid)
          .build();
    } else if (object instanceof DetectionsPackageTranslator detectionsPackageTranslator) {
      return detectionsPackageTranslator.toBuilder()
          .uuid(uuid)
          .build();
    } else if (object instanceof FileTypeTranslator fileTypeTranslator) {
      return fileTypeTranslator.toBuilder()
          .uuid(uuid)
          .build();
    } else if (object instanceof InstrumentTranslator instrumentTranslator) {
      return instrumentTranslator.toBuilder()
          .uuid(uuid)
          .build();
    } else if (object instanceof OrganizationTranslator organizationTranslator) {
      return organizationTranslator.toBuilder()
          .uuid(uuid)
          .build();
    } else if (object instanceof OtherSensorTranslator otherSensorTranslator) {
      return otherSensorTranslator.toBuilder()
          .uuid(uuid)
          .build();
    } else if (object instanceof PersonTranslator personTranslator) {
      return personTranslator.toBuilder()
          .uuid(uuid)
          .build();
    } else if (object instanceof PlatformTranslator platformTranslator) {
      return platformTranslator.toBuilder()
          .uuid(uuid)
          .build();
    } else if (object instanceof ProjectTranslator projectTranslator) {
      return projectTranslator.toBuilder()
          .uuid(uuid)
          .build();
    } else if (object instanceof SeaTranslator seaTranslator) {
      return seaTranslator.toBuilder()
          .uuid(uuid)
          .build();
    } else {
      throw new BadArgumentException(String.format(
          "Unsupported translator type %s", object.getClass().getSimpleName()
      ));
    }
  }
}
