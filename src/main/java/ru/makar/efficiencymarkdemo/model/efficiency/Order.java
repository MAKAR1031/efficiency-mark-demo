package ru.makar.efficiencymarkdemo.model.efficiency;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created by makar
 * 18.11.2018 17:39
 */
@Data
public class Order {
    private Long ticket;
    private String type;
    private LocalDateTime openTime;
    private LocalDateTime closeTime;
    private Float volume;
    private Float openPrice;
    private Float closePrice;
    private Float profit;
    private Float stopLoss;
    private Float takeProfit;
}
