package com.smartwarehouse.packaging.decorator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

// Zhanibek - Concrete Decorator calculating cargo insurance based on declared value
public class InsuranceCoverageDecorator extends PackageDecorator {
    private final double insuranceRate;

    public InsuranceCoverageDecorator(FulfillablePackage wrappedPackage, double insuranceRate) {
        super(wrappedPackage);
        if (insuranceRate <= 0.0 || insuranceRate > 0.10) {
            throw new IllegalArgumentException("Insurance rate must be between 0.0 and 0.10: " + insuranceRate);
        }
        this.insuranceRate = insuranceRate;
    }

    @Override
    public BigDecimal getHandlingFee() {
        BigDecimal declaredValue = wrappedPackage.getOrder().getDeclaredValue();
        BigDecimal premium = declaredValue
                .multiply(BigDecimal.valueOf(insuranceRate))
                .setScale(2, RoundingMode.HALF_UP);
        return wrappedPackage.getHandlingFee().add(premium);
    }

    @Override
    public List<String> getHandlingInstructions() {
        List<String> instructions = new ArrayList<>(wrappedPackage.getHandlingInstructions());
        instructions.add("Insured cargo: tamper-evident seal and recipient signature required");
        return instructions;
    }

    @Override
    public List<String> getCareTags() {
        List<String> tags = new ArrayList<>(wrappedPackage.getCareTags());
        tags.add("INSURED");
        return tags;
    }
}