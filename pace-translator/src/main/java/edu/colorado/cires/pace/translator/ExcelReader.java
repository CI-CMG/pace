package edu.colorado.cires.pace.translator;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.dhatim.fastexcel.reader.Cell;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;

public class ExcelReader {
  
  public static Stream<MapWithRowNumber> read(InputStream inputStream, int sheetIndex) throws IOException {
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
              
              return rows.stream().map(row -> rowToPropertyMap(row, headers));
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          }
      ).orElse(Stream.empty());
    }
  }
  
  private static MapWithRowNumber rowToPropertyMap(Row row, List<String> headers) {
    return new MapWithRowNumber(
        IntStream.range(0, headers.size()).boxed().collect(Collectors.toMap(
            headers::get,
            i -> new ValueWithColumnNumber(
                Optional.ofNullable(row.getCell(i))
                    .map(Cell::getRawValue),
                i
            )
        )),
        row.getRowNum()
    );
  }

}
