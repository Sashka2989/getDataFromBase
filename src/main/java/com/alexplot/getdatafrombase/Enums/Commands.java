package com.alexplot.getdatafrombase.Enums;

public enum Commands {
    SEARCH("search"), STAT("stat"), NOT_FOUND("not found");

    private String value;

    Commands(String value) {
        this.value = value;
    }

    public static Commands fromValue(String value) {
        for (final Commands dayOfWeek : values()) {
            if (dayOfWeek.value.equalsIgnoreCase(value)) {
                return dayOfWeek;
            }
        }
        return NOT_FOUND;
    }
}
