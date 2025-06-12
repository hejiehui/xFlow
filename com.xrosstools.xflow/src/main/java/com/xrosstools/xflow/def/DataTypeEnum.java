package com.xrosstools.xflow.def;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public enum DataTypeEnum {

    STRING("String"),

    INTEGER("Integer"),

    LONG("Long"),

    FLOAT("Float"),

    DOUBLE("Double"),

    BOOLEAN("Boolean"),

    DATE("Date"),

    TIME_UNIT("Time unit"),

    OBJECT("Object");

    private DataTypeEnum(String name) {
        this.name = name;
    }

    private String name;

    public String getDisplayName() {
        return name;
    }

    public static DataTypeEnum findByDisplayName(String name) {
        for(DataTypeEnum e: DataTypeEnum.values())
            if(e.getDisplayName().equals(name))
                return e;

        throw new IllegalArgumentException(name);
    }

    public Object parse(String valueStr) {
        switch (this) {
            case STRING: return valueStr;
            case INTEGER: return Integer.parseInt(valueStr);
            case LONG: return Long.parseLong(valueStr);
            case FLOAT: return Float.parseFloat(valueStr);
            case DOUBLE: return Double.parseDouble(valueStr);
            case BOOLEAN: return Boolean.parseBoolean(valueStr);
            case DATE: return new Date(valueStr);
            case TIME_UNIT: return TimeUnit.valueOf(valueStr);
            default: throw new IllegalArgumentException(this.toString());
        }
    }
}
