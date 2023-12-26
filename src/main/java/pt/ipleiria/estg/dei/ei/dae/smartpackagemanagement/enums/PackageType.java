package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums;

public enum PackageType {
    PRIMARY, SECONDARY, TERTIARY;

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
