package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.ExcelTranslator;
import edu.colorado.cires.pace.data.object.ExcelTranslatorField;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import org.apache.commons.lang3.StringUtils;

public class ExcelTranslatorForm extends TranslatorForm<ExcelTranslatorField, ExcelTranslator> {

  public ExcelTranslatorForm(ExcelTranslator initialTranslator) {
    super(initialTranslator);
  }

  @Override
  protected TranslatorFieldPanel<ExcelTranslatorField> createTranslatorFieldPanel(ExcelTranslatorField initialField,
      Consumer<TranslatorFieldPanel<ExcelTranslatorField>> translatorFieldPanelConsumer) {
    return new ExcelTranslatorFieldPanel(initialField, translatorFieldPanelConsumer);
  }

  @Override
  protected ExcelTranslator toTranslator(String uuidText, String name, List<ExcelTranslatorField> fields) {
    return ExcelTranslator.builder()
        .uuid(StringUtils.isBlank(uuidText) ? null : UUID.fromString(uuidText))
        .name(name)
        .fields(fields)
        .build();
  }
}
