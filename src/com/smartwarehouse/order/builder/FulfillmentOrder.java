package com.smartwarehouse.order.builder;

import com.smartwarehouse.model.Address;
import com.smartwarehouse.model.OrderItem;
import com.smartwarehouse.model.PriorityLevel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Yerassyl - Builder Pattern.
 * Immutable shipment order constructed and validated through FulfillmentOrder.Builder.
 */
public final class FulfillmentOrder {
    private static final double DIMENSIONAL_WEIGHT_DIVISOR = 5000.0;
    private static final double HEAVY_LIFT_LIMIT_KG = 25.0;
    private static final BigDecimal HIGH_VALUE_LIMIT_KZT = BigDecimal.valueOf(500_000.0);

    private final String orderId;
    private final String customerName;
    private final String customerEmail;
    private final Address originAddress;
    private final Address destinationAddress;
    private final PriorityLevel priority;
    private final double lengthCm;
    private final double widthCm;
    private final double heightCm;
    private final double weightKg;
    private final BigDecimal declaredValue;

    private FulfillmentOrder(Builder builder) {
        this.orderId = builder.orderId;
        this.customerName = builder.customerName;
        this.customerEmail = builder.customerEmail;
        this.originAddress = builder.originAddress;
        this.destinationAddress = builder.destinationAddress;
        this.priority = builder.priority;
        this.lengthCm = builder.lengthCm;
        this.widthCm = builder.widthCm;
        this.heightCm = builder.heightCm;
        this.weightKg = builder.weightKg;
        this.declaredValue = builder.declaredValue;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public Address getOriginAddress() {
        return originAddress;
    }

    public Address getDestinationAddress() {
        return destinationAddress;
    }

    public PriorityLevel getPriority() {
        return priority;
    }

    public double getWeightKg() {
        return weightKg;
    }

    public BigDecimal getDeclaredValue() {
        return declaredValue;
    }

    public double getDimensionalWeight() {
        return (lengthCm * widthCm * heightCm) / DIMENSIONAL_WEIGHT_DIVISOR;
    }

    public boolean hasHeavyLiftRequirement() {
        return weightKg >= HEAVY_LIFT_LIMIT_KG;
    }

    public boolean isHighValueCargo() {
        return declaredValue.compareTo(HIGH_VALUE_LIMIT_KZT) >= 0;
    }

    public static class Builder {
        private String orderId;
        private String customerName;
        private String customerEmail;
        private Address originAddress;
        private Address destinationAddress;
        private final List<OrderItem> items = new ArrayList<>();
        private PriorityLevel priority = PriorityLevel.STANDARD;
        private double lengthCm;
        private double widthCm;
        private double heightCm;
        private double weightKg;
        private BigDecimal declaredValue = BigDecimal.ZERO;

        public Builder setOrderId(String orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder setCustomerName(String customerName) {
            this.customerName = customerName;
            return this;
        }

        public Builder setCustomerEmail(String customerEmail) {
            this.customerEmail = customerEmail;
            return this;
        }

        public Builder setOriginAddress(Address originAddress) {
            this.originAddress = originAddress;
            return this;
        }

        public Builder setDestinationAddress(Address destinationAddress) {
            this.destinationAddress = destinationAddress;
            return this;
        }

        public Builder addItem(OrderItem item) {
            Objects.requireNonNull(item, "Order item cannot be null.");
            this.items.add(item);
            return this;
        }

        public Builder setPriority(PriorityLevel priority) {
            this.priority = Objects.requireNonNull(priority, "Priority cannot be null.");
            return this;
        }

        public Builder setDimensionsCm(double lengthCm, double widthCm, double heightCm) {
            this.lengthCm = lengthCm;
            this.widthCm = widthCm;
            this.heightCm = heightCm;
            return this;
        }

        public Builder setWeightKg(double weightKg) {
            this.weightKg = weightKg;
            return this;
        }

        public Builder setDeclaredValue(BigDecimal declaredValue) {
            this.declaredValue = declaredValue;
            return this;
        }

        public FulfillmentOrder build() {
            validateDetails();
            validateDimensions();
            validateItems();
            return new FulfillmentOrder(this);
        }

        private void validateDetails() {
            if (orderId == null || orderId.isBlank()) {
                throw new IllegalArgumentException("Order ID cannot be blank.");
            }
            if (customerName == null || customerName.isBlank()) {
                throw new IllegalArgumentException("Customer name cannot be blank.");
            }
            if (customerEmail == null || !customerEmail.contains("@")) {
                throw new IllegalArgumentException("A valid customer email is required.");
            }
            if (originAddress == null || destinationAddress == null) {
                throw new IllegalArgumentException("Origin and destination addresses are required.");
            }
        }

        private void validateDimensions() {
            if (lengthCm <= 0.0 || widthCm <= 0.0 || heightCm <= 0.0) {
                throw new IllegalArgumentException("Package dimensions must be positive.");
            }
            if (weightKg <= 0.0) {
                throw new IllegalArgumentException("Package weight must be positive: " + weightKg);
            }
            if (declaredValue == null || declaredValue.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Declared value cannot be negative.");
            }
        }

        private void validateItems() {
            if (items.isEmpty()) {
                throw new IllegalArgumentException("Order must contain at least one item.");
            }
        }
    }
}