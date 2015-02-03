package org.anarres.typeserializer.core;

import com.google.common.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Map;
import org.anarres.typeserializer.core.GenericNameTypeVisitor.Feature;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.*;

/**
 *
 * @author shevek
 */
public class TypeStringifierTest {

    private static final Logger LOG = LoggerFactory.getLogger(TypeStringifierTest.class);

    private static void testName(TypeVisitor<StringBuilder, ?, RuntimeException> visitor, Type in, String out) {
        LOG.info("In: " + in);
        LOG.info("Expect: " + out);
        StringBuilder buf = new StringBuilder();
        visitor.visit(in, buf);
        LOG.info("Actual: " + buf);
        assertEquals(out, buf.toString());
    }

    @Test
    public void testQualifiedName() {
        GenericNameTypeVisitor visitor = new GenericNameTypeVisitor();
        testName(visitor, String.class, "java.lang.String");
        testName(visitor, String[].class, "java.lang.String[]");
        testName(visitor, Map.class, "java.util.Map");
        testName(visitor, (new TypeToken<Map[]>() {
        }).getType(), "java.util.Map[]");
        testName(visitor, (new TypeToken<Map<String, Integer>[]>() {
        }).getType(), "java.util.Map<java.lang.String, java.lang.Integer>[]");
        testName(visitor, (new TypeToken<Map<? extends String, ? super Integer>[]>() {
        }).getType(), "java.util.Map<? extends java.lang.String, ? super java.lang.Integer>[]");
    }

    @Test
    public void testVarargsName() {
        GenericNameTypeVisitor visitor = new GenericNameTypeVisitor(Feature.UnqualifiedJavaLangImports, Feature.VarargsTopLevel);
        testName(visitor, String[].class, "String...");
        testName(visitor, Map[].class, "java.util.Map...");
        testName(visitor, (new TypeToken<Map<String, Integer>[]>() {
        }).getType(), "java.util.Map<String, Integer>...");
    }
}
