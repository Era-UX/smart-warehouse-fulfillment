package com.smartwarehouse.packaging.decorator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// Zhanibek - Concrete Decorator adding insulated dry-ice packaging for temperature control
public class ColdChainThermalDecorator extends PackageDecorator {
    private static final BigDecimal THERMAL_BOX_FEE = BigDecimal.valueOf(4800.0);
    private static final double COOLANT_WEIGHT_KG = 1.80;

    private final String temperatureRange;

    public ColdChainThermalDecorator(FulfillablePackage wrappedPackage, String temperatureRange) {
        super(wrappedPackage);
        if (temperatureRange == null || temperatureRange.isBlank()) {
            throw new IllegalArgumentException("Temperature range cannot be blank.");
        }
        this.temperatureRange = temperatureRange;
    }

    @Override
    public double getTotalWeight() {
        return wrappedPackage.getTotalWeight() + COOLANT_WEIGHT_KG;
    }

    @Override
    public BigDecimal getHandlingFee() {
        return wrappedPackage.getHandlingFee().add(THERMAL_BOX_FEE);
    }

    @Override
    public List<String> getHandlingInstructions() {
        List<String> instructions = new ArrayList<>(wrappedPackage.getHandlingInstructions());
        instructions.add("Cold chain (" + temperatureRange + "): thermal liner + temperature logger");
        return instructions;
    }

    @Override
    public List<String> getCareTags() {
        List<String> tags = new ArrayList<>(wrappedPackage.getCareTags());
        tags.add("COLD_CHAIN");
        return tags;
    }
}