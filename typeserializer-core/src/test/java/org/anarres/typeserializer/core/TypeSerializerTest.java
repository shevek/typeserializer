/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.typeserializer.core;

import com.google.common.reflect.TypeToken;
import java.util.Map;
import java.lang.reflect.Type;
import java.util.List;
import javax.annotation.Nonnull;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.*;

/**
 *
 * @author shevek
 */
public class TypeSerializerTest {

    private static final Logger LOG = LoggerFactory.getLogger(TypeSerializerTest.class);

    public static class InnerStatic {
    }

    public class InnerInstance {
    }

    public void testTransform(@Nonnull Type t) {
        LOG.info("Input is " + t + " of type " + t.getClass());
        String text0 = TypeSerializer.serialize(t);
        LOG.info("Text is " + text0);
        Type q = TypeSerializer.deserialize(text0);
        LOG.info("Output is " + q + " of type " + q.getClass());

        // Now escape the Sun type classes before re-testing.
        t = q;
        LOG.info("Input is " + t + " of type " + t.getClass());
        String text1 = TypeSerializer.serialize(t);
        LOG.info("Text is " + text1);
        q = TypeSerializer.deserialize(text1);
        LOG.info("Output is " + q + " of type " + q.getClass());

        assertEquals(t, q);
        assertEquals(text0, text1);

        StringBuilder out = new StringBuilder();
        UnqualifiedGenericNameTypeVisitor.INSTANCE.visit(t, out);
        LOG.info("Unqualified is " + out);
    }

    @Test
    public void testTransform() throws Exception {
        testTransform(byte.class);
        testTransform(byte[].class);
        testTransform(Object.class);
        testTransform(InnerStatic.class);
        testTransform(InnerInstance.class);

        testTransform((new TypeToken<Map<String, String>>() {
        }).getType());
        testTransform((new TypeToken<Map<String[], String[]>[]>() {
        }).getType());
        testTransform((new TypeToken<List<?>>() {
        }).getType());
        testTransform((new TypeToken<List<? extends String>>() {
        }).getType());
        testTransform((new TypeToken<List<? super String>>() {
        }).getType());
    }
}
