package edu.colorado.cires.pace.translator;

import edu.colorado.cires.pace.translator.TranslatorExecutor.MapWithRowNumber;
import edu.colorado.cires.pace.translator.TranslatorExecutor.ValueWithColumnNumber;
import java.io.IOException;
import java.io.Reader;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class CSVReader {
  
  public static Stream<MapWithRowNumber> read(Reader reader) throws IOException {
    CSVFormat format = CSVFormat.DEFAULT.builder()
        .setHeader()
        .setSkipHeaderRecord(true)
        .build();

    String[] headers = format.getHeader();
    
    return format.parse(reader).stream()
        .map(r -> convertRowToPropertyMap(r, headers));
  }

  private static MapWithRowNumber convertRowToPropertyMap(CSVRecord record, String[] headers) {
    return new MapWithRowNumber(
        IntStream.range(0, headers.length).boxed().collect(Collectors.toMap(
            i -> headers[i],
            i -> new ValueWithColumnNumber(Optional.ofNullable(record.get(i)), i)
        )),
        (int) record.getRecordNumber()
    );
  }

}
