/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.typeserializer.core.analysis;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import org.anarres.typeserializer.core.impl.GenericArrayTypeImpl;
import org.anarres.typeserializer.core.impl.ParameterizedTypeImpl;
import org.anarres.typeserializer.core.impl.Utils;
import org.anarres.typeserializer.core.node.AArrayType;
import org.anarres.typeserializer.core.node.ABooleanType;
import org.anarres.typeserializer.core.node.AByteType;
import org.anarres.typeserializer.core.node.ACharType;
import org.anarres.typeserializer.core.node.AClassType;
import org.anarres.typeserializer.core.node.ADoubleType;
import org.anarres.typeserializer.core.node.AFloatType;
import org.anarres.typeserializer.core.node.AIntType;
import org.anarres.typeserializer.core.node.ALongType;
import org.anarres.typeserializer.core.node.AParameterizedType;
import org.anarres.typeserializer.core.node.AShortType;
import org.anarres.typeserializer.core.node.PType;
import org.anarres.typeserializer.core.node.TIdentifier;

/**
 *
 * @author shevek
 */
public class ReifierVisitor extends DepthFirstAdapter {

    private final ClassLoader loader;
    private final StringBuilder buffer = new StringBuilder();
    private Type type;
    private boolean generic = false;

    public ReifierVisitor(@Nonnull ClassLoader loader) {
        this.loader = loader;
    }

    public Type getType() {
        return type;
    }

    @Override
    public void caseAClassType(AClassType node) {
        buffer.setLength(0);
        boolean b = false;
        for (TIdentifier identifier : node.getName()) {
            if (b)
                buffer.append('.');
            else
                b = true;
            identifier.apply(this);
        }
        for (TIdentifier identifier : node.getInnerName()) {
            buffer.append('$');
            identifier.apply(this);
        }
        String name = buffer.toString();
        try {
            type = Utils.getClass(loader, name, false);
        } catch (ClassNotFoundException e) {
            throw new TypeNotPresentException(name, e);
        }
    }

    @Override
    public void caseTIdentifier(TIdentifier node) {
        buffer.append(node.getText());
    }

    @Override
    public void caseAArrayType(AArrayType node) {
        node.getElementType().apply(this);
        if (generic)
            type = new GenericArrayTypeImpl(type);
        else if (type instanceof Class<?>)
            type = Array.newInstance((Class<?>) type, 0).getClass();
        else
            type = new GenericArrayTypeImpl(type);
    }

    @Override
    public void caseAParameterizedType(AParameterizedType node) {
        node.getRawType().apply(this);
        Class<?> raw_type = (Class<?>) type;

        // Java does something very odd with inner array parameters of generic types.
        // We attempt to duplicate that here.
        List<Type> arguments = new ArrayList<Type>();
        boolean wg = generic;
        generic = true;
        try {
            for (PType argument : node.getArguments()) {
                argument.apply(this);
                arguments.add(type);
            }
        } finally {
            generic = wg;
        }

        type = new ParameterizedTypeImpl(raw_type, arguments.toArray(new Type[arguments.size()]));
    }

    @Override
    public void caseABooleanType(ABooleanType node) {
        type = boolean.class;
    }

    @Override
    public void caseAByteType(AByteType node) {
        type = byte.class;
    }

    @Override
    public void caseACharType(ACharType node) {
        type = char.class;
    }

    @Override
    public void caseADoubleType(ADoubleType node) {
        type = double.class;
    }

    @Override
    public void caseAFloatType(AFloatType node) {
        type = float.class;
    }

    @Override
    public void caseAIntType(AIntType node) {
        type = int.class;
    }

    @Override
    public void caseALongType(ALongType node) {
        type = long.class;
    }

    @Override
    public void caseAShortType(AShortType node) {
        type = short.class;
    }
}
