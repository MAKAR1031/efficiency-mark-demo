package ru.makar.efficiencymarkdemo.model.efficiency;

import lombok.Setter;

@Setter
public class SimpleEfficiencyMark implements EfficiencyMark {
    private double totalProfit;
    private double totalLoss;
    private double profitOrdersCount;
    private double lossOrdersCount;
    private double maxProfitOrdersInRow;
    private double maxLossOrdersInRow;
    private double maxProfit;
    private double maxLoss;
    private double midd;
    private double n;
    private double minDepositForOrder;
    private double stopLossOrdersCount;
    private double averageOrderDuration;

    @Override
    public Double totalProfit() {
        return totalProfit;
    }

    @Override
    public Double totalLoss() {
        return totalLoss;
    }

    @Override
    public Double totalResult() {
        return totalProfit() - Math.abs(totalLoss());
    }

    @Override
    public Double profitOrdersCount() {
        return profitOrdersCount;
    }

    @Override
    public Double lossOrdersCount() {
        return lossOrdersCount;
    }

    @Override
    public Double ordersCount() {
        return profitOrdersCount() + lossOrdersCount();
    }

    @Override
    public Double maxProfitOrdersInRow() {
        return maxProfitOrdersInRow;
    }

    @Override
    public Double maxLossOrdersInRow() {
        return maxLossOrdersInRow;
    }

    @Override
    public Double maxProfit() {
        return maxProfit;
    }

    @Override
    public Double maxLoss() {
        return maxLoss;
    }

    @Override
    public Double profitLossRatio() {
        return profitOrdersCount() / lossOrdersCount();
    }

    @Override
    public Double averageProfit() {
        return totalProfit() / profitOrdersCount();
    }

    @Override
    public Double averageLoss() {
        return totalLoss() / lossOrdersCount();
    }

    @Override
    public Double averageResult() {
        return totalResult() / ordersCount();
    }

    @Override
    public Double averageProfitLossRatio() {
        return averageProfit() / Math.abs(averageLoss());
    }

    @Override
    public Double midd() {
        return midd;
    }

    @Override
    public Double minimumDeposit() {
        return 2 * midd() + minDepositForOrder;
    }

    @Override
    public Double returnValue() {
        return totalResult() / n / minimumDeposit();
    }

    @Override
    public Double recoveryFactor() {
        return totalResult() / n / midd();
    }

    @Override
    public Double profitFactor() {
        return totalProfit() / Math.abs(totalLoss());
    }

    @Override
    public Double stopLossOrdersCount() {
        return stopLossOrdersCount;
    }

    @Override
    public Double stopLossManagementFactor() {
        return 1 - stopLossOrdersCount() / ordersCount();
    }

    @Override
    public Double averageOrderDuration() {
        return averageOrderDuration;
    }
}
