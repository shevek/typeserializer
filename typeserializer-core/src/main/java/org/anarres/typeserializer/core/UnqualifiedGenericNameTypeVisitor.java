package org.anarres.typeserializer.core;

/**
 *
 * @author shevek
 */
public class UnqualifiedGenericNameTypeVisitor extends QualifiedGenericNameTypeVisitor {

    public static final UnqualifiedGenericNameTypeVisitor INSTANCE = new UnqualifiedGenericNameTypeVisitor();

    @Override
    protected void visitClassName(Class<?> type, StringBuilder out) {
        out.append(type.getSimpleName());
    }

}
