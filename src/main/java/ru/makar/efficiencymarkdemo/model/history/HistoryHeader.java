package ru.makar.efficiencymarkdemo.model.history;

import lombok.Data;

@Data
public class HistoryHeader {
    private int version;
    private String copyright;
    private String symbol;
    private int period;
    private int digits;
    private int createTime;
    private int lastSynchronizeTime;
}
