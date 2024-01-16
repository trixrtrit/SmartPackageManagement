package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class EnumCustomDeserializer<T extends Enum<T> & EnumCustomSerialization> extends JsonDeserializer<T> {
    private final Class<T> enumType;

    public EnumCustomDeserializer(Class<T> enumType) {
        this.enumType = enumType;
    }

    @Override
    public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String value = jsonParser.getValueAsString();
        for (T enumConstant : enumType.getEnumConstants()) {
            if (enumConstant.name().equalsIgnoreCase(value.toUpperCase())) {
                return enumConstant;
            }
        }
        throw new IllegalArgumentException("Invalid enum value: " + value);
    }
}
