package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.ExcelTranslator;
import edu.colorado.cires.pace.data.object.ExcelTranslatorField;
import edu.colorado.cires.pace.translator.excel.ExcelTranslatorFactory;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import org.apache.commons.lang3.StringUtils;

public class ExcelTranslatorFormOld extends TranslatorFormOld<ExcelTranslatorField, ExcelTranslator> {

  public ExcelTranslatorFormOld(ExcelTranslator initialTranslator) {
    super(initialTranslator, (s) -> ExcelTranslatorFactory.createTranslator(null, s));
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
