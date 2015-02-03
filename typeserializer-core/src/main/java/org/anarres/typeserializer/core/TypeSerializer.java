package org.anarres.typeserializer.core;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;
import java.lang.reflect.Type;
import javax.annotation.Nonnull;
import org.anarres.typeserializer.core.analysis.ReifierVisitor;
import org.anarres.typeserializer.core.impl.Utils;
import org.anarres.typeserializer.core.lexer.Lexer;
import org.anarres.typeserializer.core.lexer.LexerException;
import org.anarres.typeserializer.core.node.Start;
import org.anarres.typeserializer.core.parser.Parser;
import org.anarres.typeserializer.core.parser.ParserException;

/**
 *
 * @author shevek
 */
public class TypeSerializer {

    @Nonnull
    public static ClassLoader getClassLoader() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null)
            loader = TypeSerializer.class.getClassLoader();
        return loader;
    }

    @Nonnull
    public static Type deserialize(@Nonnull ClassLoader loader, @Nonnull String name) {
        try {
            StringReader sr = new StringReader(name);
            PushbackReader pr = new PushbackReader(sr, 10);
            Lexer lx = new Lexer(pr);
            Parser ps = new Parser(lx);
            Start ast = ps.parse();
            ReifierVisitor reifier = new ReifierVisitor(loader);
            ast.apply(reifier);
            Type type = reifier.getType();
            // LOG.info(name + " -> " + type);
            return type;
        } catch (ParserException e) {
            throw new TypeNotPresentException(name, e);
        } catch (LexerException e) {
            throw new TypeNotPresentException(name, e);
        } catch (IOException e) {
            throw new TypeNotPresentException(name, e);
        }
    }

    private static void serialize(@Nonnull StringBuilder out, @Nonnull Type value) {
        Utils.assertNotNull(value, "Type was null.");
        GenericNameTypeVisitor.INSTANCE.visit(value, out);
    }

    @Nonnull
    public static Type deserialize(@Nonnull String name) {
        return deserialize(getClassLoader(), name);
    }

    @Nonnull
    public static String serialize(@Nonnull Type value) {
        StringBuilder out = new StringBuilder();
        serialize(out, value);
        // LOG.debug(value + " -> " + out);
        return out.toString();
    }
}
