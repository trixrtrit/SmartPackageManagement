package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class EnumCustomSerializer extends JsonSerializer<EnumCustomSerialization> {

    @Override
    public void serialize(EnumCustomSerialization value, JsonGenerator generator, SerializerProvider serializerProvider) throws IOException {
        generator.writeString(value.toString());
    }
}
