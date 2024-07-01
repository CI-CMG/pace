package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.CSVTranslator;
import edu.colorado.cires.pace.data.object.CSVTranslatorField;
import edu.colorado.cires.pace.translator.csv.CSVTranslatorFactory;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import org.apache.commons.lang3.StringUtils;

public class CSVTranslatorFormOld extends TranslatorFormOld<CSVTranslatorField, CSVTranslator> {

  public CSVTranslatorFormOld(CSVTranslator initialTranslator) {
    super(initialTranslator, (s) -> CSVTranslatorFactory.createTranslator(null, s));
  }

  @Override
  protected TranslatorFieldPanel<CSVTranslatorField> createTranslatorFieldPanel(CSVTranslatorField initialField,
      Consumer<TranslatorFieldPanel<CSVTranslatorField>> translatorFieldPanelConsumer) {
    return new CSVTranslatorFieldPanel(initialField, translatorFieldPanelConsumer);
  }

  @Override
  protected CSVTranslator toTranslator(String uuidText, String name, List<CSVTranslatorField> fields) {
    return CSVTranslator.builder()
        .uuid(StringUtils.isBlank(uuidText) ? null : UUID.fromString(uuidText))
        .name(name)
        .fields(fields)
        .build();
  }
}
