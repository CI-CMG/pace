package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.data.translator.Translator;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.TranslatorRepository;
import edu.colorado.cires.pace.translator.converter.Converter;
import java.util.function.Function;
import javax.swing.JDialog;

public abstract class TranslatePanel<O extends ObjectWithUniqueField, T extends Translator> extends DataPanel<O> {

  private final Class<O> clazz;
  private final TranslatorRepository translatorRepository;
  private final Converter<T, O> converter;
  private final Class<T> translatorClazz;

  public TranslatePanel(CRUDRepository<O> repository, String[] headers,
      Function<O, Object[]> objectConversion,
      Class<O> clazz, TranslatorRepository translatorRepository, Converter<T, O> converter, Class<T> translatorClazz) {
    super(repository, headers, objectConversion);
    this.clazz = clazz;
    this.translatorRepository = translatorRepository;
    this.converter = converter;
    this.translatorClazz = translatorClazz;
  }

  protected void createTranslateForm()
      throws DatastoreException {
    JDialog dialog = new JDialog();
    dialog.add(new TranslateForm<>(this::loadData, repository, clazz, translatorRepository, converter, translatorClazz));
    dialog.setLocationRelativeTo(this);
    dialog.pack();
    dialog.setVisible(true);
  }
}
