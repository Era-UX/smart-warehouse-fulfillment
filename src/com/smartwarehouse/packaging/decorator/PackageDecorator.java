package com.smartwarehouse.packaging.decorator;

import com.smartwarehouse.order.builder.FulfillmentOrder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

// Zhanibek - Base Decorator wrapping a FulfillablePackage
public abstract class PackageDecorator implements FulfillablePackage {
    protected final FulfillablePackage wrappedPackage;

    protected PackageDecorator(FulfillablePackage wrappedPackage) {
        this.wrappedPackage = Objects.requireNonNull(wrappedPackage, "Wrapped package cannot be null.");
    }

    @Override
    public FulfillmentOrder getOrder() {
        return wrappedPackage.getOrder();
    }

    @Override
    public double getTotalWeight() {
        return wrappedPackage.getTotalWeight();
    }

    @Override
    public BigDecimal getHandlingFee() {
        return wrappedPackage.getHandlingFee();
    }

    @Override
    public List<String> getHandlingInstructions() {
        return wrappedPackage.getHandlingInstructions();
    }

    @Override
    public List<String> getCareTags() {
        return wrappedPackage.getCareTags();
    }
}