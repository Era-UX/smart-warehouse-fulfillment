package com.smartwarehouse.carrier.factory;

import com.smartwarehouse.order.builder.FulfillmentOrder;

import java.util.UUID;

// Temirlan - Concrete Carrier for FedEx heavy freight
public class FedExCarrier implements Carrier {

    @Override
    public String getCarrierName() {
        return "FedEx Freight";
    }

    @Override
    public String generateTrackingNumber() {
        return "FDX-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase();
    }

    @Override
    public String dispatchConsignment(FulfillmentOrder order, String handlingNotes) {
        String trackingCode = generateTrackingNumber();
        System.out.println("  [DISPATCH: " + getCarrierName() + "] Manifest: " + trackingCode
                + " | Origin Hub: " + order.getOriginAddress().postalCode()
                + " | Notes: " + handlingNotes);
        return trackingCode;
    }
}