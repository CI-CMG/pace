package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.CSVTranslatorRepository;
import edu.colorado.cires.pace.repository.ExcelTranslatorRepository;
import java.util.function.Function;
import javax.swing.JDialog;

public abstract class TranslatePanel<O extends ObjectWithUniqueField> extends DataPanel<O> {

  private final ExcelTranslatorRepository excelTranslatorRepository;
  private final CSVTranslatorRepository csvTranslatorRepository;
  private final Class<O> clazz;
  private final CRUDRepository<?>[] dependencyRepositories;

  public TranslatePanel(CRUDRepository<O> repository, String[] headers,
      Function<O, Object[]> objectConversion, ExcelTranslatorRepository excelTranslatorRepository,
      CSVTranslatorRepository csvTranslatorRepository, Class<O> clazz, CRUDRepository<?>... dependencyRepositories) {
    super(repository, headers, objectConversion);
    this.excelTranslatorRepository = excelTranslatorRepository;
    this.csvTranslatorRepository = csvTranslatorRepository;
    this.clazz = clazz;
    this.dependencyRepositories = dependencyRepositories;
  }

  protected void createTranslateForm()
      throws DatastoreException {
    JDialog dialog = new JDialog();
    dialog.add(new TranslateForm<>(this::loadData, repository, excelTranslatorRepository, csvTranslatorRepository, clazz, dependencyRepositories));
    dialog.pack();
    dialog.setVisible(true);
  }
}
