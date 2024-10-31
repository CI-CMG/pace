package edu.colorado.cires.pace.translator;

import java.util.Map;

/**
 * Holds both a map and row number
 * @param map map to hold
 * @param row row to hold
 */
public record MapWithRowNumber(Map<String, ValueWithColumnNumber> map, Integer row) {}
