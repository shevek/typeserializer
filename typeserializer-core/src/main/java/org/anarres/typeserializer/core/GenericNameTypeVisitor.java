package org.anarres.typeserializer.core;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import org.anarres.typeserializer.core.impl.Utils;

/**
 *
 * @author shevek
 */
public class GenericNameTypeVisitor extends TypeVisitor<StringBuilder, Void, RuntimeException> {

    public static final GenericNameTypeVisitor INSTANCE = new GenericNameTypeVisitor();
    private static final Type[] OBJECT_TYPE_ARRAY = new Type[]{Object.class};

    public static enum Feature {

        VarargsTopLevel, UnqualifiedImports, UnqualifiedJavaLangImports;
    }
    private final Set<Feature> features;

    public GenericNameTypeVisitor(@Nonnull Feature... features) {
        this.features = EnumSet.noneOf(Feature.class);
        this.features.addAll(Arrays.asList(features));
    }

    private boolean isVarargs() {
        return getDepth() == 1 && features.contains(Feature.VarargsTopLevel);
    }

    @Override
    public Void visitParameterizedType(ParameterizedType type, StringBuilder out) {
        visit(type.getRawType(), out);
        Type[] typeArguments = type.getActualTypeArguments();
        if (Utils.isNotEmpty(typeArguments)) {
            out.append("<");
            boolean b = false;
            for (Type ptype : typeArguments) {
                if (b)
                    out.append(", ");
                else
                    b = true;
                visit(ptype, out);
            }
            out.append(">");
        }
        return null;
    }

    @Override
    public Void visitGenericArrayType(GenericArrayType type, StringBuilder out) {
        visit(type.getGenericComponentType(), out);
        if (isVarargs())
            out.append("...");
        else
            out.append("[]");
        return null;
    }

    private void appendBounds(@Nonnull StringBuilder out, @CheckForNull Type[] bounds, @Nonnull String keyword) {
        if (Utils.isNotEmpty(bounds)) {
            out.append(keyword);
            boolean b = false;
            for (Type bound : bounds) {
                if (b)
                    out.append(" & ");
                else
                    b = true;
                visit(bound, out);
            }
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
        Type[] upperBounds = type.getUpperBounds();
        if (!Arrays.equals(upperBounds, OBJECT_TYPE_ARRAY))
            appendBounds(out, upperBounds, " extends ");
        appendBounds(out, type.getLowerBounds(), " super ");
        return null;
    }

    protected void visitClassName(@Nonnull Class<?> type, StringBuilder out) {
        if (features.contains(Feature.UnqualifiedImports))
            out.append(type.getSimpleName());
        else if (features.contains(Feature.UnqualifiedJavaLangImports) && Utils.equals("java.lang", Utils.getPackageName(type)))
            out.append(type.getSimpleName());
        else
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
        boolean varargs = array > 0 && isVarargs();
        if (varargs)
            array--;
        for (int i = 0; i < array; i++)
            out.append("[]");
        if (varargs)
            out.append("...");
        return null;
    }

}
