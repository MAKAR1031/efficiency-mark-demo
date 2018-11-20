package ru.makar.efficiencymarkdemo.model.efficiency;

public interface EfficiencyMark {
    /**
     * 1. Общая прибыль
     */
    Double totalProfit();
    /**
     * 2. Общий убыток
     */
    Double totalLoss();
    /**
     * 3. Итоговый результат
     */
    Double totalResult();
    /**
     * 4. Количество прибылных сделок
     */
    Double profitOrdersCount();
    /**
     * 5. Количество убыточных сделок
     */
    Double lossOrdersCount();
    /**
     * 6. Общее количество сделок
     */
    Double ordersCount();
    /**
     * 7. Максимальное количество прибыльных сделок подряд
     */
    Double maxProfitOrdersInRow();
    /**
     * 8. Максимальное количество убыточных сделок подряд
     */
    Double maxLossOrdersInRow();
    /**
     * 9. Максимальная прибыльная сделка
     */
    Double maxProfit();
    /**
     * 10. Максимальная убыточная сделка
     */
    Double maxLoss();
    /**
     * 11. Отношение числа прибыльных сделок к числу убыточных сделок
     */
    Double profitLossRatio();
    /**
     * 12. Средняя прибыльная сделка
     */
    Double averageProfit();
    /**
     * 13. Средняя убыточная сделка
     */
    Double averageLoss();
    /**
     * 14. Средний результат сделки
     */
    Double averageResult();
    /**
     * 15. Отношение средней прибыли к среднему убытку
     */
    Double averageProfitLossRatio();
    /**
     * 16. Максимальный внутридневной нарастающий убыток
     */
    Double midd();
    /**
     * 17. Размер минимального депозита
     */
    Double minimumDeposit();
    /**
     * 18. Отдача
     */
    Double returnValue();
    /**
     * 19. Фактор восстановления
     */
    Double recoveryFactor();
    /**
     * 20. Профит фактор
     */
    Double profitFactor();
    /**
     * 21. Количество сделок с установленным стоп-приказом стоп-лосс
     */
    Double stopLossOrdersCount();
    /**
     * 22. Степень самостоятельности управления стоп-приказом стоп-лосс
     */
    Double stopLossManagementFactor();
    /**
     * 23. Средняя продолжительность сделки в минутах
     */
    Double averageOrderDuration();
}
