package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.base.ObjectWithUniqueField;
import edu.colorado.cires.pace.data.object.base.Translator;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.TranslatorRepository;
import edu.colorado.cires.pace.translator.converter.Converter;
import java.awt.Dimension;
import java.util.function.Function;
import javax.swing.JDialog;

public abstract class TranslatePanel<O extends AbstractObject, T extends Translator> extends DataPanel<O> {

  private final Class<O> clazz;
  private final TranslatorRepository translatorRepository;
  private final Converter<T, O> converter;
  private final Class<T> translatorClazz;

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
    dialog.setModal(true);
    dialog.pack();
    dialog.setVisible(true);
  }
}
