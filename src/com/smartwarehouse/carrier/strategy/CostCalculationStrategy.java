package com.smartwarehouse.carrier.strategy;

import com.smartwarehouse.order.builder.FulfillmentOrder;

import java.math.BigDecimal;

// Temirlan - Strategy interface for calculating shipping tariffs
public interface CostCalculationStrategy {
    BigDecimal computeShippingCost(FulfillmentOrder order, double billableWeightKg);
}