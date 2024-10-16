package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.sea.Sea;
import edu.colorado.cires.pace.data.object.sea.translator.SeaTranslator;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.TranslatorRepository;
import edu.colorado.cires.pace.translator.converter.SeaConverter;
import java.awt.BorderLayout;
import java.util.UUID;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * SeaAreasPanel extends MetadataPanel and provides relevant
 * fields to sea areas
 */
public class SeaAreasPanel extends MetadataPanel<Sea, SeaTranslator> {

  /**
   * Creates sea areas panel
   * @param repository holds existing sea areas objects
   * @param translatorRepository holds existing translators
   */
  public SeaAreasPanel(CRUDRepository<Sea> repository, TranslatorRepository translatorRepository) {
    super(
        "seaAreasPanel",
        repository,
        new String[]{"UUID", "Name", "Visible"},
        sea -> new Object[]{sea.getUuid(), sea.getName(), sea.isVisible()},
        Sea.class,
        (o) -> Sea.builder()
            .uuid((UUID) o[0])
            .name((String) o[1])
            .visible((Boolean) o[2])
            .build(),
        SeaForm::new,
        translatorRepository,
        new SeaConverter(),
        SeaTranslator.class
    );
  }

  @Override
  protected JPanel createControlPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());

    return panel;
  }

  @Override
  protected String getUniqueField(Sea object) {
    return object.getName();
  }

  @Override
  protected String getHumanReadableObjectName() {
    return "Sea Area";
  }
}
