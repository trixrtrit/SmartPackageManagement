package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.serialization.EnumCustomDeserializer;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.serialization.EnumCustomSerialization;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.serialization.EnumCustomSerializer;

@JsonSerialize(using = EnumCustomSerializer.class)
@JsonDeserialize(using = MeasurementTypeDeserializer.class)
public enum MeasurementType implements EnumCustomSerialization {
    NUMERIC,
    BOOLEAN,
    GEOGRAPHIC;
}

class MeasurementTypeDeserializer extends EnumCustomDeserializer<MeasurementType> {
    public MeasurementTypeDeserializer(){
        super(MeasurementType.class);
    }
}