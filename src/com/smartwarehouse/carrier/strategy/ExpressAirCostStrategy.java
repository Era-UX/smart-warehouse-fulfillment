package com.smartwarehouse.carrier.strategy;

import com.smartwarehouse.order.builder.FulfillmentOrder;

import java.math.BigDecimal;
import java.math.RoundingMode;

// Temirlan - Concrete Strategy for air freight using dimensional or actual weight
public class ExpressAirCostStrategy implements CostCalculationStrategy {
    private static final BigDecimal BASE_RATE = BigDecimal.valueOf(5000.0);
    private static final BigDecimal RATE_PER_KG = BigDecimal.valueOf(950.0);

    @Override
    public BigDecimal computeShippingCost(FulfillmentOrder order, double billableWeightKg) {
        double chargeableWeight = Math.max(billableWeightKg, order.getDimensionalWeight());
        BigDecimal weightCost = RATE_PER_KG.multiply(BigDecimal.valueOf(chargeableWeight));
        BigDecimal subtotal = BASE_RATE.add(weightCost);
        BigDecimal multiplier = BigDecimal.valueOf(order.getPriority().getCostMultiplier());
        return subtotal.multiply(multiplier).setScale(2, RoundingMode.HALF_UP);
    }
}