/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.typeserializer.core.impl;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import javax.annotation.Nonnull;

/**
 *
 * @author shevek
 */
public class WildcardTypeImpl implements WildcardType {

    @Nonnull
    private final Type[] upperBounds;
    @Nonnull
    private final Type[] lowerBounds;

    public WildcardTypeImpl(Type[] upperBounds, Type[] lowerBounds) {
        this.upperBounds = upperBounds == null ? Utils.EMPTY_TYPE_ARRAY : upperBounds;
        this.lowerBounds = lowerBounds == null ? Utils.EMPTY_TYPE_ARRAY : lowerBounds;
    }

    @Override
    @SuppressFBWarnings("EI_EXPOSE_REP")
    public Type[] getUpperBounds() {
        return upperBounds;
    }

    @Override
    @SuppressFBWarnings("EI_EXPOSE_REP")
    public Type[] getLowerBounds() {
        return lowerBounds;
    }

    @Override
    public int hashCode() {
        return Utils.hashCode(getUpperBounds())
                ^ Utils.hashCode(getLowerBounds());
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof WildcardType))
            return false;
        WildcardType other = (WildcardType) o;
        return Arrays.equals(getUpperBounds(), other.getUpperBounds())
                && Arrays.equals(getLowerBounds(), other.getLowerBounds());
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("?");
        boolean b = false;
        for (Type bound : getUpperBounds()) {
            if (b)
                buf.append(" & ");
            else
                buf.append(" extends ");
            b = true;
            buf.append(bound);
        }
        b = false;
        for (Type bound : getLowerBounds()) {
            if (b)
                buf.append(" & ");
            else
                buf.append(" super ");
            b = true;
            buf.append(bound);
        }
        return buf.toString();
    }
}
