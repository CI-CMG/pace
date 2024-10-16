package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.platform.Platform;
import edu.colorado.cires.pace.data.object.platform.translator.PlatformTranslator;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.TranslatorRepository;
import edu.colorado.cires.pace.translator.converter.PlatformConverter;
import java.util.UUID;

/**
 * PlatformsPanel extends MetadataPanel and provides structure
 * relevant to platforms
 */
public class PlatformsPanel extends MetadataPanel<Platform, PlatformTranslator> {

  /**
   * Creates platforms panel
   * @param repository holds existing platform object
   * @param translatorRepository holds existing translators
   */
  public PlatformsPanel(CRUDRepository<Platform> repository, TranslatorRepository translatorRepository) {
    super(
        "platformsPanel",
        repository,
        new String[]{"UUID", "Name", "Visible"},
        (platform) -> new Object[]{platform.getUuid(), platform.getName(), platform.isVisible()},
        Platform.class,
        (o) -> Platform.builder()
            .uuid((UUID) o[0])
            .name((String) o[1])
            .visible((Boolean) o[2])
            .build(),
        PlatformForm::new,
        translatorRepository,
        new PlatformConverter(),
        PlatformTranslator.class
    );
  }

  @Override
  protected String getUniqueField(Platform object) {
    return object.getName();
  }

  @Override
  protected String getHumanReadableObjectName() {
    return "Platform";
  }
}
