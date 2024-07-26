package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.fileType.FileType;
import edu.colorado.cires.pace.data.object.fileType.translator.FileTypeTranslator;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.TranslatorRepository;
import edu.colorado.cires.pace.translator.converter.FileTypeConverter;
import java.util.UUID;

public class FileTypesPanel extends MetadataPanel<FileType, FileTypeTranslator> {

  public FileTypesPanel(CRUDRepository<FileType> repository, TranslatorRepository translatorRepository) {
    super(
        "fileTypesPanel",
        repository,
        new String[]{"UUID", "Type", "Comment", "Visible"},
        (fileType) -> new Object[]{fileType.getUuid(), fileType.getType(), fileType.getComment(), fileType.isVisible()},
        FileType.class,
        (objects) -> FileType.builder()
            .uuid((UUID) objects[0])
            .type((String) objects[1])
            .comment((String) objects[2])
            .visible((Boolean) objects[3])
            .build(),
        FileTypeForm::new,
        translatorRepository,
        new FileTypeConverter(),
        FileTypeTranslator.class
    );
  }

  @Override
  protected String getUniqueField(FileType object) {
    return object.getType();
  }

  @Override
  protected String getHumanReadableObjectName() {
    return "File Type";
  }
}
