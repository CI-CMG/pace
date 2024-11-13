package edu.colorado.cires.pace.translator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.dhatim.fastexcel.reader.Cell;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;

/**
 * ExcelReader provides the ability to read data from an Excel file
 */
public class ExcelReader {

  /**
   * Creates stream for reading Excel file
   * @param inputStream input stream of Excel data
   * @param sheetIndex index of sheet to read from
   * @return Stream of map with row number objects
   * @throws IOException thrown in case of error reading from Excel file
   */
  public static Stream<MapWithRowNumber> read(InputStream inputStream, int sheetIndex) throws IOException, IllegalArgumentException {
    try (ReadableWorkbook workbook = new ReadableWorkbook(inputStream)) {
      return workbook.getSheet(sheetIndex).map(
          sheet -> {
            try {
              List<Row> rows = sheet.read();
              Row headerRow = rows.get(0);
              List<String> headers = headerRow.getCells(0, headerRow.getCellCount()).stream()
                  .map(Cell::getRawValue)
                  .toList();
              
              rows.remove(0);
              return rows.stream()
                  .map(row -> rowToPropertyMap(row, headers));
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          }
      ).orElse(Stream.empty());
    }
  }
  
  private static MapWithRowNumber rowToPropertyMap(Row row, List<String> headers) {
    for (int i =0; i < headers.size(); i++) {
      if (Collections.frequency(headers, headers.get(i)) > 1){
        throw new IllegalArgumentException("Duplicate header option: " + headers.get(i));
      }
    }
    return new MapWithRowNumber(
        IntStream.range(0, headers.size()).boxed().collect(Collectors.toMap(
            headers::get,
            i -> {
              Optional<String> value = Optional.empty();
              if (row.getCellCount() >= i + 1) {
                value = Optional.ofNullable(row.getCell(i))
                    .map(Cell::getText);
              }
              return new ValueWithColumnNumber(
                  value,
                  i
              );
            }
        )),
        row.getRowNum()
    );
  }

}
