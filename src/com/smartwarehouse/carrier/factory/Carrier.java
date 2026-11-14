package com.smartwarehouse.carrier.factory;

import com.smartwarehouse.order.builder.FulfillmentOrder;

// Temirlan - Product interface for Factory Method pattern
public interface Carrier {
    String getCarrierName();
    String generateTrackingNumber();
    String dispatchConsignment(FulfillmentOrder order, String handlingNotes);
}