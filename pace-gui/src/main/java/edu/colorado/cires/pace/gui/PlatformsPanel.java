package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.Platform;
import edu.colorado.cires.pace.data.translator.PlatformTranslator;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.TranslatorRepository;
import edu.colorado.cires.pace.translator.converter.PlatformConverter;
import java.util.UUID;

public class PlatformsPanel extends MetadataPanel<Platform, PlatformTranslator> {

  public PlatformsPanel(CRUDRepository<Platform> repository, TranslatorRepository translatorRepository) {
    super(
        "platformsPanel",
        repository,
        new String[]{"UUID", "Name"},
        (platform) -> new Object[]{platform.getUuid(), platform.getName()},
        Platform.class,
        (o) -> Platform.builder()
            .uuid((UUID) o[0])
            .name((String) o[1])
            .build(),
        PlatformForm::new,
        translatorRepository,
        new PlatformConverter(),
        PlatformTranslator.class
    );
  }
}
