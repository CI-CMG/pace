package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.CSVTranslator;
import edu.colorado.cires.pace.data.object.CSVTranslatorField;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import org.apache.commons.lang3.StringUtils;

public class CSVTranslatorForm extends TranslatorForm<CSVTranslatorField, CSVTranslator> {

  public CSVTranslatorForm(CSVTranslator initialTranslator) {
    super(initialTranslator);
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
