package ru.makar.efficiencymarkdemo.model.history;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HistoryBar {
    LocalDateTime openTime;
    double open;
    double high;
    double low;
    double close;
    long volume;
    int spread;
    long realVolume;
}
