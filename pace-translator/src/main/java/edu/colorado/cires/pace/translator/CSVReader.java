package edu.colorado.cires.pace.translator;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * CSVReader provides the ability to read data from a csv file
 */
public class CSVReader {

  /**
   * Creates stream for reading csv file
   * @param reader reads csv files
   * @return Stream of map with row number objects
   * @throws IOException thrown in case of error reading from csv file
   */
  public static Stream<MapWithRowNumber> read(Reader reader) throws IOException {
    CSVFormat format = CSVFormat.DEFAULT.builder()
        .setSkipHeaderRecord(false)
        .build();

    CSVParser parser = format.parse(reader);
    Iterator<CSVRecord> iterator = parser.iterator();
    CSVRecord record = iterator.next();
    String[] headers = record.values();
    return parser.stream()
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
