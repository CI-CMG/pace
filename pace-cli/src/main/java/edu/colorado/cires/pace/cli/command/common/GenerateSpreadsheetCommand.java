package edu.colorado.cires.pace.cli.command.common;

import edu.colorado.cires.pace.data.object.TabularTranslationField;
import edu.colorado.cires.pace.data.object.TabularTranslator;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.NotFoundException;
import edu.colorado.cires.pace.translator.SpreadsheetGenerator;
import edu.colorado.cires.pace.utilities.ApplicationPropertyResolver;
import edu.colorado.cires.pace.utilities.SerializationUtils;
import java.io.File;
import java.io.IOException;

public abstract class GenerateSpreadsheetCommand<F extends TabularTranslationField, T extends TabularTranslator<F>> implements Runnable {
  
  protected abstract File getFile();
  protected abstract String getTranslatorName();
  protected abstract RepositoryFactory<T> getRepositoryFactory();
  protected abstract SpreadsheetGenerator<F, T> getGenerator();
  
  protected final ApplicationPropertyResolver applicationPropertyResolver = new ApplicationPropertyResolver();

  @Override
  public void run() {
    try {
      CRUDRepository<T> repository = getRepositoryFactory().createRepository(
          applicationPropertyResolver.getDataDir(), SerializationUtils.createObjectMapper()
      );

      T translator = repository.getByUniqueField(getTranslatorName());

      getGenerator().generateSpreadsheet(
          getFile().toPath(), translator
      );
    } catch (IOException | NotFoundException | DatastoreException e) {
      throw new RuntimeException(e);
    }
  }
}
