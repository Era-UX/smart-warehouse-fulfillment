package com.smartwarehouse.carrier.factory;

// Temirlan - Concrete Creator for DHL Express
public class DHLCarrierFactory extends CarrierFactory {
    @Override
    public Carrier createCarrier() {
        return new DHLCarrier();
    }
}