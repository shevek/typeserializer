package org.anarres.typeserializer.core;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import javax.annotation.Nonnull;
import org.anarres.typeserializer.core.impl.Utils;

/**
 *
 * @author shevek
 */
public class TypeVisitor<I, O, X extends Exception> {

    public O visit(@Nonnull Type value, I input) throws X {
        Utils.assertNotNull(value, "Type was null.");
        if (value instanceof ParameterizedType) {
            return visitParameterizedType((ParameterizedType) value, input);
        } else if (value instanceof GenericArrayType) {
            return visitGenericArrayType((GenericArrayType) value, input);
        } else if (value instanceof Class<?>) {
            return visitClass((Class<?>) value, input);
        } else if (value instanceof TypeVariable<?>) {
            return visitTypeVariable((TypeVariable<?>) value, input);
        } else if (value instanceof WildcardType) {
            return visitWildcardType((WildcardType) value, input);
        } else {
            throw new IllegalArgumentException("Unknown type " + value);
        }
    }

    public O visitParameterizedType(@Nonnull ParameterizedType value, I input) throws X {
        for (Type type : value.getActualTypeArguments())
            visit(type, input);
        return visitDefault(value, input);
    }

    public O visitGenericArrayType(@Nonnull GenericArrayType value, I input) throws X {
        visit(value.getGenericComponentType(), input);
        return visitDefault(value, input);
    }

    public O visitClass(@Nonnull Class<?> value, I input) throws X {
        if (value.isArray())
            visit(value.getComponentType(), input);
        return visitDefault(value, input);
    }

    public O visitTypeVariable(TypeVariable<?> value, I input) throws X {
        for (Type bound : value.getBounds())
            visit(bound, input);
        return visitDefault(value, input);
    }

    public O visitWildcardType(WildcardType value, I input) throws X {
        for (Type bound : value.getLowerBounds())
            visit(bound, input);
        for (Type bound : value.getUpperBounds())
            visit(bound, input);
        return visitDefault(value, input);
    }

    public O visitDefault(Type type, I input) throws X {
        return null;
    }
}
