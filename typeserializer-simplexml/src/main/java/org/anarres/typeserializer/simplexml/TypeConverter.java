/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.typeserializer.simplexml;

import java.lang.reflect.Type;
import javax.annotation.Nonnull;
import org.anarres.typeserializer.core.TypeSerializer;
import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

/**
 *
 * @author shevek
 */
public class TypeConverter extends AbstractConverter implements Converter<Type> {

    public static class Inner {

        private static final TypeConverter INSTANCE = new TypeConverter();
    }

    @Nonnull
    public static TypeConverter getInstance() {
        return Inner.INSTANCE;
    }
    public static final String ATTRIBUTE = "name";

    @Override
    public Type read(InputNode node) throws Exception {
        String name = getAttribute(node, ATTRIBUTE);
        return TypeSerializer.deserialize(name);
    }

    @Override
    public void write(OutputNode node, Type value) throws Exception {
        String name = TypeSerializer.serialize(value);
        node.setAttribute(ATTRIBUTE, name);
    }
}
