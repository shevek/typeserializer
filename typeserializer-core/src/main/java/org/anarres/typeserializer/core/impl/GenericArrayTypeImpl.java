/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.typeserializer.core.impl;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import javax.annotation.Nonnull;

/**
 *
 * @author shevek
 */
public class GenericArrayTypeImpl implements GenericArrayType {

    @Nonnull
    private final Type componentType;

    public GenericArrayTypeImpl(@Nonnull Type componentType) {
        this.componentType = componentType;
    }

    @Override
    public Type getGenericComponentType() {
        return componentType;
    }

    @Override
    public int hashCode() {
        return componentType.hashCode() << 1;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (o == null)
            return false;
        if (!(o instanceof GenericArrayType))
            return false;
        GenericArrayType other = (GenericArrayType) o;
        return Utils.equals(getGenericComponentType(), other.getGenericComponentType());
    }

    @Override
    public String toString() {
        return getGenericComponentType() + "[]";
    }
}
