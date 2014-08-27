package org.anarres.typeserializer.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.lang.reflect.Type;
import org.anarres.typeserializer.core.TypeSerializer;

/**
 *
 * @author shevek
 */
public class KryoTypeSerializer extends Serializer<Type> {

    @Override
    public void write(Kryo kryo, Output output, Type object) {
        String text = TypeSerializer.serialize(object);
        output.writeString(text);
    }

    @Override
    public Type read(Kryo kryo, Input input, Class<Type> type) {
        String text = input.readString();
        return TypeSerializer.deserialize(TypeSerializer.getClassLoader(), text);
    }

}
