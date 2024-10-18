package edu.colorado.cires.pace.packaging;

import java.nio.file.Path;

/**
 * Holds a source and target path
 * @param source reference location
 * @param target target location
 */
record PackageInstruction(Path source, Path target) {}
