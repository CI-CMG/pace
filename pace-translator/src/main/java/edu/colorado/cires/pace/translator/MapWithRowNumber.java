package edu.colorado.cires.pace.translator;

import java.util.Map;

public record MapWithRowNumber(Map<String, ValueWithColumnNumber> map, Integer row) {}
