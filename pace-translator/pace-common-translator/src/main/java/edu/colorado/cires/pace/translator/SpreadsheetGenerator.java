package edu.colorado.cires.pace.translator;

import edu.colorado.cires.pace.data.object.TabularTranslationField;
import edu.colorado.cires.pace.data.object.TabularTranslator;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class SpreadsheetGenerator<F extends TabularTranslationField, T extends TabularTranslator<F>> {

  private static final Pattern WORD_FINDER = Pattern.compile("(([A-Z]?[a-z]+)|([A-Z]))|(\\(\\d\\))");
  
  public void generateSpreadsheet(Path outputPath, T translator) throws IOException {
    try (OutputStream outputStream = new FileOutputStream(outputPath.toFile())) {
      writeFieldsToSpreadsheet(outputStream, translator.getFields());
    }
  }
  
  protected String[] getHeaderNames(List<F> translatorFields) {
    return translatorFields.stream()
        .sorted(Comparator.comparing(F::getColumnNumber))
        .map(F::getPropertyName)
        .map(SpreadsheetGenerator::camelCaseToHumanReadableHeader)
        .toArray(String[]::new);
  }
  
  private static String camelCaseToHumanReadableHeader(String camelCase) {
    List<String> header = new ArrayList<>(0);
    String[] fields = camelCase.split("\\.");
    for (String rawFieldName : fields) {
      String field = rawFieldName;
      field = field.replace("[", " (");
      field = field.replace("]", ")");

      header.add(findWordsInMixedCase(field));
    }
    
    return String.join(" ", header);
  }

  private static String findWordsInMixedCase(String text) {
    Matcher matcher = WORD_FINDER.matcher(text);
    List<String> words = new ArrayList<>();
    while (matcher.find()) {
      words.add(capitalizeFirst(matcher.group(0)));
    }
    return String.join(" ", words);
  }

  private static String capitalizeFirst(String word) {
    return word.substring(0, 1).toUpperCase()
        + word.substring(1).toLowerCase();
  }


  protected abstract void writeFieldsToSpreadsheet(OutputStream outputStream, List<F> translatorFields) throws IOException;

}
