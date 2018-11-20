package ru.makar.efficiencymarkdemo.model.history;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class HistoryData {
    private HistoryHeader header;
    private List<HistoryBar> bars;

    public HistoryData(HistoryHeader header, List<HistoryBar> bars) {
        this.header = header;
        this.bars = bars;
    }
}
