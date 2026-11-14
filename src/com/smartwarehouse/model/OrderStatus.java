package com.smartwarehouse.model;

// Yerassyl - Lifecycle states tracked by order observers
public enum OrderStatus {
    CREATED,
    INSPECTED_AND_PACKED,
    CARRIER_ASSIGNED,
    IN_TRANSIT,
    EXCEPTION_ENCOUNTERED
}