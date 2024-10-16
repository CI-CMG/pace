package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.detectionType.DetectionType;
import edu.colorado.cires.pace.data.object.detectionType.translator.DetectionTypeTranslator;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.TranslatorRepository;
import edu.colorado.cires.pace.translator.converter.DetectionTypeConverter;
import java.util.UUID;

/**
 * DetectionTypesPanel extends MetadataPanel and structures the form panel for
 * detection types
 */
public class DetectionTypesPanel extends MetadataPanel<DetectionType, DetectionTypeTranslator> {

  /**
   * Initializes a detection types panel
   * @param repository relevant data repository
   * @param translatorRepository relevant translator repository
   */
  public DetectionTypesPanel(CRUDRepository<DetectionType> repository, TranslatorRepository translatorRepository) {
    super(
        "detectionTypesPanel",
        repository,
        new String[]{
            "UUID", "Source", "Science Name", "Visible"
        },
        (detectionType) -> new Object[] {
            detectionType.getUuid(), detectionType.getSource(), detectionType.getScienceName(), detectionType.isVisible()
        },
        DetectionType.class,
        (objects) -> DetectionType.builder()
            .uuid((UUID) objects[0])
            .source((String) objects[1])
            .scienceName((String) objects[2])
            .visible((Boolean) objects[3])
            .build(),
        DetectionTypeForm::new,
        translatorRepository,
        new DetectionTypeConverter(),
        DetectionTypeTranslator.class
    );
  }

  @Override
  protected String getUniqueField(DetectionType object) {
    return object.getSource();
  }

  @Override
  protected String getHumanReadableObjectName() {
    return "Detection Type";
  }
}
