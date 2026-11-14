package com.smartwarehouse.carrier.factory;

// Temirlan - Concrete Creator for FedEx
public class FedExCarrierFactory extends CarrierFactory {
    @Override
    public Carrier createCarrier() {
        return new FedExCarrier();
    }
}