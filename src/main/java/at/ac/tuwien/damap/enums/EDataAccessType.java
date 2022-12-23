package at.ac.tuwien.damap.enums;

import java.util.HashMap;

public enum EDataAccessType {

    OPEN("open"),
    RESTRICTED("restricted"),
    CLOSED("closed");

    private final String value;

    private static final HashMap<String, EDataAccessType> MAP = new HashMap<>();

    EDataAccessType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public String getValue() {
        return this.value;
    }

    public static EDataAccessType getByValue(String value) {
        return MAP.get(value);
    }

    static {
        for (EDataAccessType type : EDataAccessType.values()) {
            MAP.put(type.getValue(), type);
        }
    }
}
