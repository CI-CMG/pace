package edu.colorado.cires.pace.data.translator;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.cires.pace.data.ObjectWithUniqueFieldTest;

class FileTypeTranslatorTest extends ObjectWithUniqueFieldTest<FileTypeTranslator> {

  @Override
  protected FileTypeTranslator createObject() {
    return FileTypeTranslator.builder().build();
  }
}