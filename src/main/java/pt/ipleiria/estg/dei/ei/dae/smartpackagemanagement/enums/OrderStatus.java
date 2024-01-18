package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.serialization.EnumCustomDeserializer;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.serialization.EnumCustomSerialization;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.serialization.EnumCustomSerializer;

@JsonSerialize(using = EnumCustomSerializer.class)
@JsonDeserialize(using = OrderStatusDeserializer.class)
public enum OrderStatus implements EnumCustomSerialization {
    PENDING,
    REJECTED,
    ACCEPTED,
    COMPLETED
}

class OrderStatusDeserializer extends EnumCustomDeserializer<OrderStatus> {
    public OrderStatusDeserializer(){
        super(OrderStatus.class);
    }
}
