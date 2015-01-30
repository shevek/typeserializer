/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.typeserializer.core.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 *
 * @author shevek
 */
public class ParameterizedTypeImpl implements ParameterizedType {

    @Nonnull
    private final Class<?> rawType;
    @Nonnull
    private final Type[] typeArguments;

    public ParameterizedTypeImpl(
            @Nonnull Class<?> rawType,
            @Nonnull Type... typeArguments) {
        this.rawType = rawType;
        this.typeArguments = (typeArguments == null) ? Utils.EMPTY_TYPE_ARRAY : typeArguments;
    }

    @Override
    @CheckForNull
    public Type getOwnerType() {
        return null;
    }

    @Override
    @Nonnull
    public Class<?> getRawType() {
        return rawType;
    }

    @Override
    @SuppressWarnings("EI_EXPOSE_REP")
    public Type[] getActualTypeArguments() {
        return typeArguments;
    }

    @Override
    public int hashCode() {
        return Utils.hashCode(getOwnerType())
                ^ Utils.hashCode(getRawType())
                ^ Arrays.hashCode(getActualTypeArguments());
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ParameterizedType))
            return false;
        ParameterizedType other = (ParameterizedType) o;
        return Utils.equals(getOwnerType(), other.getOwnerType())
                && Utils.equals(getRawType(), other.getRawType())
                && Arrays.equals(getActualTypeArguments(), other.getActualTypeArguments());
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(rawType.getName());
        buf.append("<");
        boolean b = false;
        for (Type type : getActualTypeArguments()) {
            if (b)
                buf.append(", ");
            else
                b = true;
            buf.append(type);
        }
        buf.append(">");
        return buf.toString();
    }
}
