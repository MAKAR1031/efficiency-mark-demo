package ru.makar.efficiencymarkdemo.model.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by makar
 * 18.11.2018 19:04
 */
@RequiredArgsConstructor
public enum RowType {
    SELL("sell"),
    BUY("buy"),
    TP("t/p"),
    SL("s/l"),
    MODIFY("modify"),
    CLOSE_AT_STOP("close at stop"),
    INVALID("invalid");

    @Getter
    private final String name;

    public static RowType byName(String name) {
        for (RowType value : values()) {
            if (name.equals(value.getName())) {
                return value;
            }
        }
        return INVALID;
    }
}
