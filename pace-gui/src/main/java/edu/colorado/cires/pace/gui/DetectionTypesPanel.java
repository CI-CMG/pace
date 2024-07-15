package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.DetectionType;
import edu.colorado.cires.pace.data.translator.DetectionTypeTranslator;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.TranslatorRepository;
import edu.colorado.cires.pace.translator.converter.DetectionTypeConverter;
import java.util.UUID;

public class DetectionTypesPanel extends MetadataPanel<DetectionType, DetectionTypeTranslator> {

  public DetectionTypesPanel(CRUDRepository<DetectionType> repository, TranslatorRepository translatorRepository) {
    super(
        "detectionTypesPanel",
        repository,
        new String[]{
            "UUID", "Source", "Science Name"
        },
        (detectionType) -> new Object[] {
            detectionType.getUuid(), detectionType.getSource(), detectionType.getScienceName()
        },
        DetectionType.class,
        (objects) -> DetectionType.builder()
            .uuid((UUID) objects[0])
            .source((String) objects[1])
            .scienceName((String) objects[2])
            .build(),
        DetectionTypeForm::new,
        translatorRepository,
        new DetectionTypeConverter(),
        DetectionTypeTranslator.class
    );
  }
}
