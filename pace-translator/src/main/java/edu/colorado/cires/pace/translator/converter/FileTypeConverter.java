package edu.colorado.cires.pace.translator.converter;

import static edu.colorado.cires.pace.translator.converter.ConversionUtils.stringFromMap;
import static edu.colorado.cires.pace.translator.converter.ConversionUtils.uuidFromMap;

import edu.colorado.cires.pace.data.object.fileType.FileType;
import edu.colorado.cires.pace.data.object.fileType.translator.FileTypeTranslator;
import edu.colorado.cires.pace.translator.ValueWithColumnNumber;
import java.util.Map;

public class FileTypeConverter extends Converter<FileTypeTranslator, FileType> {

  @Override
  public FileType convert(FileTypeTranslator translator, Map<String, ValueWithColumnNumber> properties, int row,
      RuntimeException runtimeException) {
    return FileType.builder()
        .uuid(uuidFromMap(properties, translator.getFileTypeUUID(), row, runtimeException))
        .type(stringFromMap(properties, translator.getType()))
        .comment(stringFromMap(properties, translator.getComment()))
        .build();
  }
}
