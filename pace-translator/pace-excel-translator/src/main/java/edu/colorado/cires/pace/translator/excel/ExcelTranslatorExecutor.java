package edu.colorado.cires.pace.translator.excel;

import edu.colorado.cires.pace.data.object.ExcelTranslator;
import edu.colorado.cires.pace.data.object.ExcelTranslatorField;
import edu.colorado.cires.pace.translator.TranslationException;
import edu.colorado.cires.pace.translator.TranslatorExecutor;
import edu.colorado.cires.pace.translator.TranslatorValidationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.NotImplementedException;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;

public class ExcelTranslatorExecutor<O> extends TranslatorExecutor<O, ExcelTranslator> {

  public ExcelTranslatorExecutor(ExcelTranslator translatorDefinition, Class<O> clazz) throws TranslatorValidationException {
    super(translatorDefinition, clazz);
  }

  @Override
  protected Stream<MapWithRowNumber> getPropertyStream(InputStream inputStream, ExcelTranslator translatorDefinition)
      throws IOException {
    try (ReadableWorkbook readableWorkbook = new ReadableWorkbook(inputStream)) {
      Map<Integer, List<ExcelTranslatorField>> sheetTranslatorMappings = translatorDefinition.getFields().stream()
          .map(f -> f.toBuilder()
              .sheetNumber(f.getColumnNumber() - 1)
              .columnNumber(f.getSheetNumber() - 1)
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
          .map(rows -> convertRowsToPropertyMap(rows, sheetTranslatorMappings));
    }
  }

  @Override
  protected Stream<MapWithRowNumber> getPropertyStream(Reader reader, ExcelTranslator translatorDefinition) {
    throw new NotImplementedException("Reading excel sheets from Reader not implemented");
  }

  private MapWithRowNumber convertRowsToPropertyMap(List<RowWithSheetIndex> rows, Map<Integer, List<ExcelTranslatorField>> sheetTranslatorMappings) {
    HashMap<String, Optional<String>> objectMap = new HashMap<>(0);
    
    Integer rowNumber = null;
    for (RowWithSheetIndex row : rows) {
      rowNumber = row.row().getRowNum();
      List<ExcelTranslatorField> fields = sheetTranslatorMappings.get(row.sheetIndex);
      for (ExcelTranslatorField field : fields) {
        objectMap.put(
            field.getPropertyName(),
            row.row().getCellAsString(field.getColumnNumber())
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
