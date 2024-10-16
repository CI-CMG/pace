package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.base.Translator;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.TranslatorRepository;
import edu.colorado.cires.pace.translator.converter.Converter;
import java.awt.Dimension;
import java.util.function.Function;
import javax.swing.JDialog;

/**
 * TranslatePanel extends DataPanel and provides structure relevant to translate panel
 * @param <O> Object type
 * @param <T> Translator type
 */
public abstract class TranslatePanel<O extends AbstractObject, T extends Translator> extends DataPanel<O> {

  private final Class<O> clazz;
  private final TranslatorRepository translatorRepository;
  private final Converter<T, O> converter;
  private final Class<T> translatorClazz;

  /**
   * Creates a translate panel
   * @param name panel name
   * @param repository repository holding existing data
   * @param headers headers in panel table
   * @param objectConversion creates an object list out of translator fields
   * @param clazz class type
   * @param translatorRepository repository holding existing translators
   * @param converter creates object when provided translator and relevant data column
   * @param translatorClazz type of translators
   */
  public TranslatePanel(String name, CRUDRepository<O> repository, String[] headers,
      Function<O, Object[]> objectConversion,
      Class<O> clazz, TranslatorRepository translatorRepository, Converter<T, O> converter, Class<T> translatorClazz) {
    super(name, repository, headers, objectConversion);
    this.clazz = clazz;
    this.translatorRepository = translatorRepository;
    this.converter = converter;
    this.translatorClazz = translatorClazz;
  }

  protected void createTranslateForm()
      throws DatastoreException {
    JDialog dialog = new JDialog();
    dialog.setName("translateDialog");
    TranslateForm<O, T> translateForm = new TranslateForm<>(this::searchData, repository, clazz, translatorRepository, converter, translatorClazz);
    translateForm.init();
    dialog.add(translateForm);
    dialog.setTitle("Translate");
    Dimension size = UIUtils.getPercentageOfWindowDimension(0.5, 0.4);
    dialog.setSize(size);
    dialog.setPreferredSize(size);
    dialog.setLocationRelativeTo(this);
    dialog.pack();
    dialog.setVisible(true);
  }
}
