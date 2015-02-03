package org.anarres.typeserializer.core.impl;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 *
 * @author shevek
 */
public class Utils {

    public static final Type[] EMPTY_TYPE_ARRAY = new Type[0];

    /**
     * The package separator character: <code>'&#x2e;' == {@value}</code>.
     */
    public static final char PACKAGE_SEPARATOR_CHAR = '.';

    /**
     * The inner class separator character: <code>'$' == {@value}</code>.
     */
    public static final char INNER_CLASS_SEPARATOR_CHAR = '$';

    public static boolean isNotEmpty(@CheckForNull Object[] a) {
        if (a == null)
            return false;
        return a.length > 0;
    }

    public static boolean equals(@CheckForNull Object a, @CheckForNull Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

    public static int hashCode(@CheckForNull Object o) {
        return o != null ? o.hashCode() : 0;
    }

    /**
     * Maps a primitive class name to its corresponding abbreviation used in array class names.
     */
    private static final Map<String, String> abbreviationMap;

    /**
     * Feed abbreviation maps
     */
    static {
        final Map<String, String> m = new HashMap<String, String>();
        m.put("int", "I");
        m.put("boolean", "Z");
        m.put("float", "F");
        m.put("long", "J");
        m.put("short", "S");
        m.put("byte", "B");
        m.put("double", "D");
        m.put("char", "C");
        m.put("void", "V");
        abbreviationMap = Collections.unmodifiableMap(m);
    }

    @Nonnull
    public static String getPackageName(@Nonnull Class<?> c) {
        String className = c.getName();
        int i = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
        if (i == -1)
            return "";
        return className.substring(0, i);
    }

    // ----------------------------------------------------------------------
    /**
     * Converts a class name to a JLS style class name.
     *
     * @param className  the class name
     * @return the converted name
     */
    @Nonnull
    private static String toCanonicalName(@Nonnull String className) {
        if (className.endsWith("[]")) {
            final StringBuilder classNameBuffer = new StringBuilder();
            while (className.endsWith("[]")) {
                className = className.substring(0, className.length() - 2);
                classNameBuffer.append("[");
            }
            final String abbreviation = abbreviationMap.get(className);
            if (abbreviation != null) {
                classNameBuffer.append(abbreviation);
            } else {
                classNameBuffer.append("L").append(className).append(";");
            }
            className = classNameBuffer.toString();
        }
        return className;
    }

    // Class loading
    // ----------------------------------------------------------------------
    /**
     * Returns the class represented by {@code className} using the
     * {@code classLoader}.  This implementation supports the syntaxes
     * "{@code java.util.Map.Entry[]}", "{@code java.util.Map$Entry[]}",
     * "{@code [Ljava.util.Map.Entry;}", and "{@code [Ljava.util.Map$Entry;}".
     *
     * @param classLoader  the class loader to use to load the class
     * @param className  the class name
     * @param initialize  whether the class must be initialized
     * @return the class represented by {@code className} using the {@code classLoader}
     * @throws ClassNotFoundException if the class is not found
     */
    public static Class<?> getClass(
            final ClassLoader classLoader, final String className, final boolean initialize) throws ClassNotFoundException {
        try {
            Class<?> clazz;
            if (abbreviationMap.containsKey(className)) {
                final String clsName = "[" + abbreviationMap.get(className);
                clazz = Class.forName(clsName, initialize, classLoader).getComponentType();
            } else {
                clazz = Class.forName(toCanonicalName(className), initialize, classLoader);
            }
            return clazz;
        } catch (final ClassNotFoundException ex) {
            // allow path separators (.) as inner class name separators
            final int lastDotIndex = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);

            if (lastDotIndex != -1) {
                try {
                    return getClass(classLoader, className.substring(0, lastDotIndex)
                            + INNER_CLASS_SEPARATOR_CHAR + className.substring(lastDotIndex + 1),
                            initialize);
                } catch (final ClassNotFoundException ex2) { // NOPMD
                    // ignore exception
                }
            }

            throw ex;
        }
    }

    public static void assertNotNull(@CheckForNull Object object, @Nonnull String message) {
        if (object == null)
            throw new NullPointerException(message);
    }
}
