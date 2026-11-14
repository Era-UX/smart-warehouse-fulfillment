package com.smartwarehouse.carrier.factory;

import com.smartwarehouse.order.builder.FulfillmentOrder;

import java.util.UUID;

// Temirlan - Concrete Carrier for DHL Express air shipments
public class DHLCarrier implements Carrier {

    @Override
    public String getCarrierName() {
        return "DHL Express Air";
    }

    @Override
    public String generateTrackingNumber() {
        return "DHL-" + UUID.randomUUID().toString().substring(0, 9).toUpperCase();
    }

    @Override
    public String dispatchConsignment(FulfillmentOrder order, String handlingNotes) {
        String trackingCode = generateTrackingNumber();
        System.out.println("  [DISPATCH: " + getCarrierName() + "] Waybill: " + trackingCode
                + " | Route: " + order.getOriginAddress().city() + " -> " + order.getDestinationAddress().city()
                + " | Notes: " + handlingNotes);
        return trackingCode;
    }
}