/*
 * Code formatter project
 * CS 4481
 */
package submit.ast;

import java.util.ArrayList;

/**
 *
 * @author edwajohn
 */
public interface Statement extends Node {
    public default boolean isCompound() {
        return false;
    }
}
