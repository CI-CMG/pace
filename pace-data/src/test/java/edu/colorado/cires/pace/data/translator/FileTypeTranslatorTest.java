package edu.colorado.cires.pace.data.translator;

import edu.colorado.cires.pace.data.ObjectWithUniqueFieldTest;
import edu.colorado.cires.pace.data.object.fileType.translator.FileTypeTranslator;

class FileTypeTranslatorTest extends ObjectWithUniqueFieldTest<FileTypeTranslator> {

  @Override
  protected FileTypeTranslator createObject() {
    return FileTypeTranslator.builder().build();
  }
}