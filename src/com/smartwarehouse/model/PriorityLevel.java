package com.smartwarehouse.model;

// Yerassyl - Delivery priority tiers and tariff multipliers
public enum PriorityLevel {
    STANDARD(1.0),
    EXPEDITED(1.5),
    CRITICAL_URGENT(2.0);

    private final double costMultiplier;

    PriorityLevel(double costMultiplier) {
        this.costMultiplier = costMultiplier;
    }

    public double getCostMultiplier() {
        return costMultiplier;
    }
}