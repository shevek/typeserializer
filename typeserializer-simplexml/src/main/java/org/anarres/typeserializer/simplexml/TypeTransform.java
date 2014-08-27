/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.typeserializer.simplexml;

import java.lang.reflect.Type;
import javax.annotation.Nonnull;
import org.anarres.typeserializer.core.TypeSerializer;
import org.simpleframework.xml.transform.Transform;

/**
 *
 * @author shevek
 */
public class TypeTransform implements Transform<Type> {

    public static class Inner {

        private static final TypeTransform INSTANCE = new TypeTransform();
    }

    @Nonnull
    public static TypeTransform getInstance() {
        return Inner.INSTANCE;
    }

    @Override
    public Type read(String value) throws Exception {
        return TypeSerializer.deserialize(TypeSerializer.getClassLoader(), value);
    }

    @Override
    public String write(Type value) throws Exception {
        return TypeSerializer.serialize(value);
    }
}
