package com.smartwarehouse.packaging.decorator;

import com.smartwarehouse.order.builder.FulfillmentOrder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// Zhanibek - Concrete Component representing a base unwrapped package
public class StandardOrderPackage implements FulfillablePackage {
    private final FulfillmentOrder order;

    public StandardOrderPackage(FulfillmentOrder order) {
        this.order = Objects.requireNonNull(order, "Order cannot be null.");
    }

    @Override
    public FulfillmentOrder getOrder() {
        return order;
    }

    @Override
    public double getTotalWeight() {
        return order.getWeightKg();
    }

    @Override
    public BigDecimal getHandlingFee() {
        return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public List<String> getHandlingInstructions() {
        List<String> instructions = new ArrayList<>();
        instructions.add("Standard carton packaging");
        return instructions;
    }

    @Override
    public List<String> getCareTags() {
        List<String> tags = new ArrayList<>();
        tags.add("STANDARD");
        return tags;
    }
}