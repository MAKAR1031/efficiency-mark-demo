package ru.makar.efficiencymarkdemo;

import lombok.extern.slf4j.Slf4j;
import ru.makar.efficiencymarkdemo.model.data.DataRow;
import ru.makar.efficiencymarkdemo.model.data.ParsedDataRow;
import ru.makar.efficiencymarkdemo.model.efficiency.EfficiencyMark;
import ru.makar.efficiencymarkdemo.model.efficiency.Order;
import ru.makar.efficiencymarkdemo.model.efficiency.Orders;
import ru.makar.efficiencymarkdemo.model.efficiency.SimpleEfficiencyMark;
import ru.makar.efficiencymarkdemo.model.history.HistoryBar;
import ru.makar.efficiencymarkdemo.model.history.HistoryData;
import ru.makar.efficiencymarkdemo.model.history.HistoryFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static ru.makar.efficiencymarkdemo.model.data.RowType.CLOSE_AT_STOP;

@Slf4j
public class Main {
    private static final String RESULTS_DIR = "data/results/";
    private static final String HISTORY_FILE = "data/history/EURUSD.hst";
    private static final String OUTPUT_PATH = "data/output/report.txt";
    private static final Double LOT_SIZE = 100000.0;
    private static final Integer LEVERAGE = 200;

    public static void main(String[] args) throws IOException {
        log.info("read history from {}", HISTORY_FILE);
        HistoryData history = new HistoryFile(HISTORY_FILE).read(2018);
        log.info("history was read successfully");
        log.info("header: {}", history.getHeader());
        log.info("bars: {}", history.getBars().size());
        List<String> results = new LinkedList<>();
        log.info("scan result directory {}", RESULTS_DIR);
        Files.newDirectoryStream(Paths.get(RESULTS_DIR)).forEach(path -> {
            try {
                EfficiencyMark mark = mark(history, path);
                String result = mark.toString().replaceAll("\\.", ",");
                results.add(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Path outputPath = Paths.get(OUTPUT_PATH);
        log.info("all results processed, writing it to report {}", outputPath);
        if (Files.exists(outputPath)) {
            log.info("file exists, remove it");
            Files.delete(outputPath);
        }
        Files.write(outputPath, results, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        log.info("complete");
    }

    private static EfficiencyMark mark(HistoryData history, Path file) throws IOException {
        log.info("read result file {}", file);
        List<DataRow> data = Files.readAllLines(file)
                .stream()
                .map(ParsedDataRow::new)
                .map(ParsedDataRow::parse)
                .collect(Collectors.toList());
        log.info("{} rows was read and parsed", data.size());
        log.info("filtering data...");
        List<Long> filteredTickets = data.stream()
                .filter(row -> row.getType() == CLOSE_AT_STOP)
                .map(DataRow::getTicket)
                .sorted()
                .collect(Collectors.toList());
        log.info("filtered tickets: {}", filteredTickets.stream().map(Objects::toString).collect(Collectors.joining(", ")));
        log.info("filtered tickets count: {}", filteredTickets.size());
        List<DataRow> filteredData = data.stream()
                .filter(item -> !filteredTickets.contains(item.getTicket()))
                .collect(Collectors.toList());
        log.info("filtered data size: {}", filteredData.size());
        Orders orders = new Orders();
        filteredData.forEach(orders::addInfo);
        SimpleEfficiencyMark mark = new SimpleEfficiencyMark();
        List<Order> list = orders.list();
        LocalDate startDate = list.get(0).getOpenTime().toLocalDate().withDayOfMonth(1);
        LocalDate endDate = list.get(list.size() - 1)
                .getCloseTime()
                .toLocalDate()
                .plusMonths(1)
                .withDayOfMonth(1);
        log.info("system work period: [{} - {}]", startDate, endDate);
        long n = ChronoUnit.MONTHS.between(startDate, endDate);
        log.info("total month: {}", n);
        log.info("calculate efficiency...");
        mark.setTotalProfit(list.stream().mapToDouble(Order::getProfit).filter(value -> value >= 0).sum());
        mark.setTotalLoss(list.stream().mapToDouble(Order::getProfit).filter(value -> value < 0).sum());
        mark.setProfitOrdersCount(list.stream().filter(order -> order.getProfit() >= 0).count());
        mark.setLossOrdersCount(list.stream().filter(order -> order.getProfit() < 0).count());
        mark.setMaxProfitOrdersInRow(maxOrdersInRow(list, order -> order.getProfit() > 0));
        mark.setMaxLossOrdersInRow(maxOrdersInRow(list, order -> order.getProfit() < 0));
        mark.setMaxProfit(list.stream().mapToDouble(Order::getProfit).max().orElse(0));
        mark.setMaxLoss(list.stream().mapToDouble(Order::getProfit).min().orElse(0));
        mark.setMidd(calculateMidd(list, history));
        mark.setN(n);
        mark.setMinDepositForOrder(calculateMinDeposit(list));
        mark.setStopLossOrdersCount(list.stream().filter(order -> order.getStopLoss() > 0).count());
        mark.setAverageOrderDuration(list.stream()
                .mapToLong(Main::duration)
                .average()
                .orElse(0));
        log.info("efficiency calculated");
        return mark;
    }

    private static int maxOrdersInRow(List<Order> orders, Predicate<Order> predicate) {
        int max = 0;
        int count = 0;
        for (Order order : orders) {
            if (predicate.test(order)) {
                count++;
            } else {
                if (count > max) {
                    max = count;
                }
                count = 0;
            }
        }
        return max;
    }

    private static double calculateMidd(List<Order> orders, HistoryData history) {
        double max = 0;
        for (Order order : orders) {
            double extremum = findExtremum(history, order.getType(), order.getOpenTime(), order.getCloseTime());
            if (extremum == 0) {
                continue;
            }
            double contract = order.getVolume() * LOT_SIZE;
            double value = "sell".equals(order.getType()) ?
                    contract * order.getOpenPrice() - contract * extremum :
                    contract * extremum - contract * order.getOpenPrice();
            if (value < 0 && Math.abs(value) > max) {
                max = Math.abs(value);
            }
        }
        return max;
    }

    private static double findExtremum(
            HistoryData history,
            String orderType,
            LocalDateTime openTime,
            LocalDateTime closeTime
    ) {
        return orderType.equals("sell") ?
                findSellExtremum(history, openTime, closeTime) :
                findBuyExtremum(history, openTime, closeTime);
    }

    private static double findSellExtremum(
            HistoryData history,
            LocalDateTime openTime,
            LocalDateTime closeTime
    ) {
        return history.getBars().stream()
                .filter(b -> b.getOpenTime().isAfter(openTime))
                .filter(b -> b.getOpenTime().isBefore(closeTime))
                .mapToDouble(HistoryBar::getHigh)
                .max()
                .orElse(0);
    }

    private static double findBuyExtremum(
            HistoryData history,
            LocalDateTime openTime,
            LocalDateTime closeTime
    ) {
        return history.getBars().stream()
                .filter(b -> b.getOpenTime().isAfter(openTime))
                .filter(b -> b.getOpenTime().isBefore(closeTime))
                .mapToDouble(HistoryBar::getLow)
                .min()
                .orElse(0);
    }

    private static double calculateMinDeposit(List<Order> orders) {
        Order order = orders.stream().max(Comparator.comparing(Order::getVolume)).orElseThrow(RuntimeException::new);
        return LOT_SIZE * order.getVolume() / LEVERAGE * order.getOpenPrice();
    }

    private static long duration(Order order) {
        Duration duration = Duration.between(order.getOpenTime(), order.getCloseTime());
        LocalDate currentDate = order.getOpenTime().toLocalDate();
        LocalDate endDate = order.getCloseTime().toLocalDate();
        if (currentDate.equals(endDate)) {
            return duration.toMinutes();
        }
        int holidays = 0;
        do {
            currentDate = currentDate.plusDays(1);
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
            if (dayOfWeek == SATURDAY || dayOfWeek == SUNDAY) {
                holidays++;
            }
        } while (!currentDate.equals(endDate));
        return duration.minusDays(holidays).toMinutes();
    }
}
