package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.serialization.EnumCustomDeserializer;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.serialization.EnumCustomSerialization;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.serialization.EnumCustomSerializer;

@JsonSerialize(using = EnumCustomSerializer.class)
@JsonDeserialize(using = DeliveryStatusDeserializer.class)
public enum DeliveryStatus implements EnumCustomSerialization {
    PROCESSING,
    DISPATCHED,
    INTRANSIT,
    DELIVERED
}

class DeliveryStatusDeserializer extends EnumCustomDeserializer<DeliveryStatus> {
    public DeliveryStatusDeserializer(){
        super(DeliveryStatus.class);
    }
}
