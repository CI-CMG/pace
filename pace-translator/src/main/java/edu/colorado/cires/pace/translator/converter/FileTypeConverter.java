package edu.colorado.cires.pace.translator.converter;

import static edu.colorado.cires.pace.translator.converter.ConversionUtils.stringFromMap;
import static edu.colorado.cires.pace.translator.converter.ConversionUtils.uuidFromMap;

import edu.colorado.cires.pace.data.object.fileType.FileType;
import edu.colorado.cires.pace.data.object.fileType.translator.FileTypeTranslator;
import edu.colorado.cires.pace.translator.ValueWithColumnNumber;
import java.util.Map;

/**
 * FileTypeConverter extends Converter and provides convert function for
 * file type objects
 */
public class FileTypeConverter extends Converter<FileTypeTranslator, FileType> {

  /**
   * Creates a file type object from the provided properties
   * @param translator translates to file type object
   * @param properties maps property names to values
   * @param row relevant row
   * @param runtimeException thrown in case of error mapping
   * @return FileType object
   */
  @Override
  public FileType convert(FileTypeTranslator translator, Map<String, ValueWithColumnNumber> properties, int row,
      RuntimeException runtimeException) {
    return FileType.builder()
        .uuid(uuidFromMap(properties, "UUID", translator.getFileTypeUUID(), row, runtimeException))
        .type(stringFromMap(properties, translator.getType()))
        .comment(stringFromMap(properties, translator.getComment()))
        .build();
  }
}
