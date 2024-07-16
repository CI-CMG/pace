package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.FileType;
import edu.colorado.cires.pace.data.translator.FileTypeTranslator;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.TranslatorRepository;
import edu.colorado.cires.pace.repository.search.FileTypeSearchParameters;
import edu.colorado.cires.pace.repository.search.SearchParameters;
import edu.colorado.cires.pace.translator.converter.FileTypeConverter;
import java.util.List;
import java.util.UUID;

public class FileTypesPanel extends MetadataPanel<FileType, FileTypeTranslator> {

  public FileTypesPanel(CRUDRepository<FileType> repository, TranslatorRepository translatorRepository) {
    super(
        "fileTypesPanel",
        repository,
        new String[]{"UUID", "Type", "Comment"},
        (fileType) -> new Object[]{fileType.getUuid(), fileType.getType(), fileType.getComment()},
        FileType.class,
        (objects) -> FileType.builder()
            .uuid((UUID) objects[0])
            .type((String) objects[1])
            .comment((String) objects[2])
            .build(),
        FileTypeForm::new,
        translatorRepository,
        new FileTypeConverter(),
        FileTypeTranslator.class
    );
  }

  @Override
  protected SearchParameters<FileType> getSearchParameters(List<String> uniqueFieldSearchTerms) {
    return FileTypeSearchParameters.builder()
        .types(uniqueFieldSearchTerms)
        .build();
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
