package edu.colorado.cires.pace.translator.excel;

import edu.colorado.cires.pace.data.object.ExcelTranslator;
import edu.colorado.cires.pace.data.object.ExcelTranslatorField;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.translator.TranslatorExecutor;
import edu.colorado.cires.pace.translator.TranslatorValidationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.dhatim.fastexcel.reader.Cell;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;

public class ExcelTranslatorExecutor<O> extends TranslatorExecutor<O, ExcelTranslator> {

  public ExcelTranslatorExecutor(ExcelTranslator translatorDefinition, Class<O> clazz, CRUDRepository<?>... dependencyRepositories) throws TranslatorValidationException {
    super(translatorDefinition, clazz, dependencyRepositories);
  }

  @Override
  protected Stream<MapWithRowNumber> getPropertyStream(InputStream inputStream, ExcelTranslator translatorDefinition)
      throws IOException {
    try (ReadableWorkbook readableWorkbook = new ReadableWorkbook(inputStream)) {
      Map<Integer, List<ExcelTranslatorField>> sheetTranslatorMappings = translatorDefinition.getFields().stream()
          .map(f -> f.toBuilder()
              .sheetNumber(f.getSheetNumber() - 1)
              .columnNumber(f.getColumnNumber() - 1)
              .build())
          .collect(Collectors.groupingBy(ExcelTranslatorField::getSheetNumber));

      return readableWorkbook.getSheets()
          .filter(sheet -> sheetTranslatorMappings.containsKey(sheet.getIndex()))
          .flatMap(sheet -> {
            try {
              return sheet.openStream()
                  .map(row -> new RowWithSheetIndex(sheet.getIndex(), row));
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          })
          .collect(Collectors.groupingBy(
              rowWithSheetIndex -> rowWithSheetIndex.row().getRowNum()
          )).values().stream()
          .map(rows -> convertRowsToPropertyMap(rows, sheetTranslatorMappings))
          .filter(Objects::nonNull);
    }
  }

  @Override
  protected Stream<MapWithRowNumber> getPropertyStream(Reader reader, ExcelTranslator translatorDefinition) {
    throw new NotImplementedException("Reading excel sheets from Reader not implemented");
  }

  private MapWithRowNumber convertRowsToPropertyMap(List<RowWithSheetIndex> rows, Map<Integer, List<ExcelTranslatorField>> sheetTranslatorMappings) {
    HashMap<String, Optional<String>> objectMap = new HashMap<>(0);
    
    if (rows.stream().allMatch(rowWithSheetIndex -> rowWithSheetIndex.row().stream().map(Cell::getRawValue).allMatch(StringUtils::isBlank))) {
      return null;
    }
    
    Integer rowNumber = null;
    for (RowWithSheetIndex row : rows) {
      rowNumber = row.row().getRowNum();
      List<ExcelTranslatorField> fields = sheetTranslatorMappings.get(row.sheetIndex);
      for (ExcelTranslatorField field : fields) {
        objectMap.put(
            field.getPropertyName(),
            Optional.ofNullable(row.row().getCellText(field.getColumnNumber()))
        );
      }
    }
    
    return new MapWithRowNumber(
        objectMap,
        rowNumber
    );
  } 

  public record RowWithSheetIndex(int sheetIndex, Row row) {}

}
