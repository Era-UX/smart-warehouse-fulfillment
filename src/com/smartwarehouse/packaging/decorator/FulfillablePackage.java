package com.smartwarehouse.packaging.decorator;

import com.smartwarehouse.order.builder.FulfillmentOrder;

import java.math.BigDecimal;
import java.util.List;

// Zhanibek - Component interface for Decorator pattern
public interface FulfillablePackage {
    FulfillmentOrder getOrder();
    double getTotalWeight();
    BigDecimal getHandlingFee();
    List<String> getHandlingInstructions();
    List<String> getCareTags();
}