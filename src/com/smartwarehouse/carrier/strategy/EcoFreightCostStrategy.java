package com.smartwarehouse.carrier.strategy;

import com.smartwarehouse.order.builder.FulfillmentOrder;

import java.math.BigDecimal;
import java.math.RoundingMode;

// Temirlan - Concrete Strategy for discounted consolidated green freight
public class EcoFreightCostStrategy implements CostCalculationStrategy {
    private static final BigDecimal BASE_RATE = BigDecimal.valueOf(1000.0);
    private static final BigDecimal RATE_PER_KG = BigDecimal.valueOf(250.0);
    private static final BigDecimal GREEN_DISCOUNT = BigDecimal.valueOf(0.85);

    @Override
    public BigDecimal computeShippingCost(FulfillmentOrder order, double billableWeightKg) {
        BigDecimal weightCost = RATE_PER_KG.multiply(BigDecimal.valueOf(billableWeightKg));
        BigDecimal discountedSubtotal = BASE_RATE.add(weightCost).multiply(GREEN_DISCOUNT);
        BigDecimal multiplier = BigDecimal.valueOf(order.getPriority().getCostMultiplier());
        return discountedSubtotal.multiply(multiplier).setScale(2, RoundingMode.HALF_UP);
    }
}