package com.smartwarehouse.carrier.factory;

import com.smartwarehouse.order.builder.FulfillmentOrder;

import java.util.UUID;

// Temirlan - Concrete Carrier for KazPost domestic shipping
public class KazPostCarrier implements Carrier {

    @Override
    public String getCarrierName() {
        return "KazPost National Logistics";
    }

    @Override
    public String generateTrackingNumber() {
        return "KZ" + UUID.randomUUID().toString().substring(0, 8).toUpperCase() + "AST";
    }

    @Override
    public String dispatchConsignment(FulfillmentOrder order, String handlingNotes) {
        String trackingCode = generateTrackingNumber();
        System.out.println("  [DISPATCH: " + getCarrierName() + "] Tracking: " + trackingCode
                + " | To: " + order.getDestinationAddress().city()
                + " | Notes: " + handlingNotes);
        return trackingCode;
    }
}