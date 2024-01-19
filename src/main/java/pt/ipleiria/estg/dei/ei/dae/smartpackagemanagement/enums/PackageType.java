package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.serialization.EnumCustomDeserializer;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.serialization.EnumCustomSerialization;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.serialization.EnumCustomSerializer;

import static jakarta.mail.Provider.Type.TRANSPORT;

@JsonSerialize(using = EnumCustomSerializer.class)
@JsonDeserialize(using = PackageTypeDeserializer.class)
public enum PackageType implements EnumCustomSerialization {
    PRIMARY,
    SECONDARY,
    TERTIARY;

    public String getDisplayType() {
        switch(this) {
            case PRIMARY:
                return "Primary";
            case SECONDARY:
                return "Secondary";
            case TERTIARY:
                return "Tertiary";
            default:
                return "";
        }
    }
}

class PackageTypeDeserializer extends EnumCustomDeserializer<PackageType> {
    public PackageTypeDeserializer(){
        super(PackageType.class);
    }
}
