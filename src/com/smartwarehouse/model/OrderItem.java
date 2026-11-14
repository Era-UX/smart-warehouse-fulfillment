package com.smartwarehouse.model;

import java.math.BigDecimal;

// Yerassyl - Line item inside a fulfillment order
public record OrderItem(
        String sku,
        String description,
        int quantity,
        BigDecimal unitPrice,
        double unitWeightKg
) {
    public OrderItem {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("SKU cannot be blank.");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Item description cannot be blank.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero: " + quantity);
        }
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Unit price cannot be negative.");
        }
        if (unitWeightKg <= 0.0) {
            throw new IllegalArgumentException("Unit weight must be positive: " + unitWeightKg);
        }
    }
}