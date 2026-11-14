package com.smartwarehouse.carrier.factory;

// Temirlan - Concrete Creator for KazPost
public class KazPostCarrierFactory extends CarrierFactory {
    @Override
    public Carrier createCarrier() {
        return new KazPostCarrier();
    }
}