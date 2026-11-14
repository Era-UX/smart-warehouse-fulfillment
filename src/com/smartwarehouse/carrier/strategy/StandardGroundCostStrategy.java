package com.smartwarehouse.carrier.strategy;

import com.smartwarehouse.order.builder.FulfillmentOrder;

import java.math.BigDecimal;
import java.math.RoundingMode;

// Temirlan - Concrete Strategy for standard regional ground delivery
public class StandardGroundCostStrategy implements CostCalculationStrategy {
    private static final BigDecimal BASE_RATE = BigDecimal.valueOf(1500.0);
    private static final BigDecimal RATE_PER_KG = BigDecimal.valueOf(350.0);

    @Override
    public BigDecimal computeShippingCost(FulfillmentOrder order, double billableWeightKg) {
        BigDecimal weightCost = RATE_PER_KG.multiply(BigDecimal.valueOf(billableWeightKg));
        BigDecimal subtotal = BASE_RATE.add(weightCost);
        BigDecimal multiplier = BigDecimal.valueOf(order.getPriority().getCostMultiplier());
        return subtotal.multiply(multiplier).setScale(2, RoundingMode.HALF_UP);
    }
}