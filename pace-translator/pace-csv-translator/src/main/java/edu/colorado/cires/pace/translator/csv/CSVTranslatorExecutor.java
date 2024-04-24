package edu.colorado.cires.pace.translator.csv;

import edu.colorado.cires.pace.data.object.CSVTranslator;
import edu.colorado.cires.pace.data.object.CSVTranslatorField;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.translator.TranslatorExecutor;
import edu.colorado.cires.pace.translator.TranslatorValidationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.NotImplementedException;

public class CSVTranslatorExecutor<O> extends TranslatorExecutor<O, CSVTranslator> {

  public CSVTranslatorExecutor(CSVTranslator translatorDefinition, Class<O> clazz, CRUDRepository<?>... dependencyRepositories) throws TranslatorValidationException {
    super(translatorDefinition, clazz, dependencyRepositories);
  }

  @Override
  protected Stream<MapWithRowNumber> getPropertyStream(InputStream inputStream, CSVTranslator translatorDefinition) {
    throw new NotImplementedException("Reading CSV rows from InputStream not implemented");
  }

  @Override
  protected Stream<MapWithRowNumber> getPropertyStream(Reader reader, CSVTranslator translatorDefinition) throws IOException {
    CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
        .setHeader()
        .setSkipHeaderRecord(true)
        .build();

    return csvFormat.parse(reader).stream()
        .map(r -> convertRowToPropertyMap(r, translatorDefinition));
  }

  private static MapWithRowNumber convertRowToPropertyMap(CSVRecord record, CSVTranslator translator) {
    return new MapWithRowNumber(
        translator.getFields().stream()
            .collect(Collectors.toMap(
                CSVTranslatorField::getPropertyName,
                f -> Optional.ofNullable(
                    record.get(f.getColumnNumber() - 1)
                )
            )),
        (int) record.getRecordNumber()
    );
  }
}
