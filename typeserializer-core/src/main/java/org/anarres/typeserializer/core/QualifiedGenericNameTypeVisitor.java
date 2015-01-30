package org.anarres.typeserializer.core;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import javax.annotation.Nonnull;

/**
 *
 * @author shevek
 */
public class QualifiedGenericNameTypeVisitor extends TypeVisitor<StringBuilder, Void, RuntimeException> {

    public static final QualifiedGenericNameTypeVisitor INSTANCE = new QualifiedGenericNameTypeVisitor();

    @Override
    public Void visitParameterizedType(ParameterizedType type, StringBuilder out) {
        visit(type.getRawType(), out);
        out.append("<");
        boolean b = false;
        for (Type ptype : type.getActualTypeArguments()) {
            if (b)
                out.append(", ");
            else
                b = true;
            visit(ptype, out);
        }
        out.append(">");
        return null;
    }

    @Override
    public Void visitGenericArrayType(GenericArrayType type, StringBuilder out) {
        visit(type.getGenericComponentType(), out);
        out.append("[]");
        return null;
    }

    private void appendBounds(StringBuilder out, Type[] bounds, String keyword) {
        boolean b = false;
        for (Type bound : bounds) {
            if (Object.class.equals(bound))
                continue;
            if (b)
                out.append(" & ");
            else
                out.append(keyword);
            b = true;
            visit(bound, out);
        }
    }

    @Override
    public Void visitTypeVariable(TypeVariable<?> type, StringBuilder out) {
        out.append(type.getName());
        appendBounds(out, type.getBounds(), " extends ");
        return null;
    }

    @Override
    public Void visitWildcardType(WildcardType type, StringBuilder out) {
        out.append("?");
        appendBounds(out, type.getUpperBounds(), " extends ");
        appendBounds(out, type.getLowerBounds(), " super ");
        return null;
    }

    protected void visitClassName(@Nonnull Class<?> type, StringBuilder out) {
        out.append(type.getName());
    }

    @Override
    public Void visitClass(Class<?> type, StringBuilder out) {
        int array = 0;
        while (type.isArray()) {
            type = type.getComponentType();
            array++;
        }
        visitClassName(type, out);
        for (int i = 0; i < array; i++) {
            out.append("[]");
        }
        return null;
    }

}
