package com.smartwarehouse.packaging.decorator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// Zhanibek - Concrete Decorator adding shock-absorbent cushioning and fragile tags
public class FragileHandlingDecorator extends PackageDecorator {
    private static final BigDecimal FRAGILE_FEE = BigDecimal.valueOf(2500.0);
    private static final double CUSHION_WEIGHT_KG = 0.45;

    public FragileHandlingDecorator(FulfillablePackage wrappedPackage) {
        super(wrappedPackage);
    }

    @Override
    public double getTotalWeight() {
        return wrappedPackage.getTotalWeight() + CUSHION_WEIGHT_KG;
    }

    @Override
    public BigDecimal getHandlingFee() {
        return wrappedPackage.getHandlingFee().add(FRAGILE_FEE);
    }

    @Override
    public List<String> getHandlingInstructions() {
        List<String> instructions = new ArrayList<>(wrappedPackage.getHandlingInstructions());
        instructions.add("Fragile: double-layer bubble wrap, do not stack");
        return instructions;
    }

    @Override
    public List<String> getCareTags() {
        List<String> tags = new ArrayList<>(wrappedPackage.getCareTags());
        tags.add("FRAGILE");
        return tags;
    }
}