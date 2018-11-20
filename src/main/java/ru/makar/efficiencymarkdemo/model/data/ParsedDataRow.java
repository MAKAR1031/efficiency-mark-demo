package ru.makar.efficiencymarkdemo.model.data;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by makar
 * 18.11.2018 17:26
 */
@RequiredArgsConstructor
public class ParsedDataRow {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");

    private final String line;

    public DataRow parse() {
        String[] parts = line.split("\t");
        DataRow row = new DataRow();
        row.setRow(Long.parseLong(parts[0]));
        row.setTime(LocalDateTime.parse(parts[1], FORMATTER));
        row.setType(RowType.byName(parts[2]));
        row.setTicket(Long.parseLong(parts[3]));
        row.setVolume(Float.parseFloat(parts[4]));
        row.setPrice(Float.parseFloat(parts[5]));
        row.setStopLoss(Float.parseFloat(parts[6]));
        row.setTakeProfit(Float.parseFloat(parts[7]));
        row.setProfit(Float.parseFloat(parts[8]));
        row.setBalance(Float.parseFloat(parts[9]));
        return row;
    }
}
