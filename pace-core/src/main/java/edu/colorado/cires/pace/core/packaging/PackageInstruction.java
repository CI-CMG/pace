package edu.colorado.cires.pace.core.packaging;

import java.nio.file.Path;

record PackageInstruction(Path source, Path target) {}
