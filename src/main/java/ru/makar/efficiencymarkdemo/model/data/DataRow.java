package ru.makar.efficiencymarkdemo.model.data;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DataRow {
    private Long row;
    private LocalDateTime time;
    private RowType type;
    private Long ticket;
    private Float volume;
    private Float price;
    private Float stopLoss;
    private Float takeProfit;
    private Float profit;
    private Float balance;
}
