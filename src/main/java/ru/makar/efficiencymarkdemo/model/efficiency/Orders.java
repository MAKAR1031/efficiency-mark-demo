package ru.makar.efficiencymarkdemo.model.efficiency;

import ru.makar.efficiencymarkdemo.model.data.DataRow;

import java.util.LinkedList;
import java.util.List;

import static ru.makar.efficiencymarkdemo.model.data.RowType.*;

/**
 * Created by makar
 * 18.11.2018 17:52
 */
public class Orders {
    private final List<Order> orders;

    public Orders() {
        orders = new LinkedList<>();
    }

    public void addInfo(DataRow row) {
        Order order = null;
        if (row.getType() == SELL || row.getType() == BUY) {
            order = new Order();
            order.setTicket(row.getTicket());
            order.setVolume(row.getVolume());
            order.setType(row.getType().getName());
            order.setOpenPrice(row.getPrice());
            order.setOpenTime(row.getTime());
            orders.add(order);
        }
        if (row.getType() == TP || row.getType() == SL || row.getType() == CLOSE_AT_STOP) {
            order = orders.stream()
                    .filter(o -> o.getTicket().equals(row.getTicket()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Order not found"));
            order.setClosePrice(row.getPrice());
            order.setCloseTime(row.getTime());
            order.setProfit(row.getProfit());
        }
        if (order != null) {
            order.setStopLoss(row.getStopLoss());
            order.setTakeProfit(row.getTakeProfit());
        }
    }

    public List<Order> list() {
        return orders;
    }
}
