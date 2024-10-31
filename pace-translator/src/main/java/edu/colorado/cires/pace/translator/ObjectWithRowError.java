package edu.colorado.cires.pace.translator;

import edu.colorado.cires.pace.data.object.base.AbstractObject;

/**
 * Holds an object, its row location, and the error related to it
 * @param object object to hold with error
 * @param row row location of object
 * @param throwable error related to object
 * @param <O> Type of object
 */
public record ObjectWithRowError<O extends AbstractObject>(O object, int row, java.lang.Throwable throwable) {}
