/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.typeserializer.core;

import com.google.common.reflect.TypeToken;
import java.util.Map;
import java.lang.reflect.Type;
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

    public void testTransform(@Nonnull Type t) {
        LOG.info("Input is " + t + " of type " + t.getClass());
        String text = TypeSerializer.serialize(t);
        LOG.info("Text is " + t);
        Type q = TypeSerializer.deserialize(text);
        LOG.info("Output is " + q + " of type " + q.getClass());

        // Now escape the Sun type classes before re-testing.
        t = q;
        LOG.info("Input is " + t + " of type " + t.getClass());
        text = TypeSerializer.serialize(t);
        LOG.info("Text is " + t);
        q = TypeSerializer.deserialize(text);
        LOG.info("Output is " + q + " of type " + q.getClass());

        assertEquals(t, q);
    }

    @Test
    public void testTransform() throws Exception {
        testTransform(byte.class);
        testTransform(byte[].class);
        testTransform(Object.class);

        testTransform((new TypeToken<Map<String, String>>() {
        }).getType());
        testTransform((new TypeToken<Map<String[], String[]>[]>() {
        }).getType());
    }
}
