package org.anarres.typeserializer.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.google.common.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Map;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.*;

/**
 *
 * @author shevek
 */
public class KryoTypeSerializerTest {

    private static final Logger LOG = LoggerFactory.getLogger(KryoTypeSerializerTest.class);

    private static Type testSerialization(Type in) {
        Kryo kryo = new Kryo();
        kryo.addDefaultSerializer(Type.class, new KryoTypeSerializer());

        LOG.info("In: " + in);
        Output output = new Output(16384);
        kryo.writeObject(output, in); // , new KryoTypeSerializer());
        output.flush();

        LOG.info("Wrote " + output.position() + " bytes.");

        Input input = new Input(output.getBuffer(), 0, output.position());
        Type out = kryo.readObject(input, Type.class); // , new KryoTypeSerializer());
        LOG.info("Out: " + out);
        LOG.info("");

        assertEquals(in, out);

        return out;
    }

    @Test
    public void testSerialization() throws Exception {
        testSerialization(int.class);
        testSerialization(int[].class);
        testSerialization(Map.class);
        testSerialization(Map[].class);
        testSerialization(new TypeToken<Map<String, Long>>() {
        }.getType());
        testSerialization(new TypeToken<Map<String, Long>[]>() {
        }.getType());
    }

}
