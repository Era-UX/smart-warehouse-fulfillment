package com.smartwarehouse;

import com.smartwarehouse.carrier.factory.CarrierFactory;
import com.smartwarehouse.carrier.factory.DHLCarrierFactory;
import com.smartwarehouse.carrier.factory.FedExCarrierFactory;
import com.smartwarehouse.carrier.factory.KazPostCarrierFactory;
import com.smartwarehouse.carrier.strategy.CostCalculationStrategy;
import com.smartwarehouse.carrier.strategy.EcoFreightCostStrategy;
import com.smartwarehouse.carrier.strategy.ExpressAirCostStrategy;
import com.smartwarehouse.facade.WarehouseFulfillmentFacade;
import com.smartwarehouse.model.Address;
import com.smartwarehouse.model.OrderItem;
import com.smartwarehouse.model.PriorityLevel;
import com.smartwarehouse.order.builder.FulfillmentOrder;

import java.math.BigDecimal;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== Smart Warehouse Fulfillment & Logistics System ===\n");

        WarehouseFulfillmentFacade warehouseFacade = WarehouseFulfillmentFacade.createDefaultFacade();

        runScenarioOne(warehouseFacade);
        runScenarioTwo(warehouseFacade);
        runScenarioThree(warehouseFacade);
        runValidationTest();

        System.out.println("All fulfillment scenarios finished");
    }

    // Scenario 1: Standard ground order via KazPost
    private static void runScenarioOne(WarehouseFulfillmentFacade facade) {
        System.out.println("[Scenario 1] Standard Ground Order (KazPost)");

        Address origin = new Address("Mangilik El 55/11", "Astana", "Astana", "010000", "Kazakhstan");
        Address destination = new Address("Dostyk Ave 180", "Almaty", "Almaty", "050000", "Kazakhstan");

        FulfillmentOrder order = new FulfillmentOrder.Builder()
                .setOrderId("ORD-101")
                .setCustomerName("Arman Baizhanov")
                .setCustomerEmail("arman.baizhanov@astanait.edu.kz")
                .setOriginAddress(origin)
                .setDestinationAddress(destination)
                .setPriority(PriorityLevel.STANDARD)
                .setDimensionsCm(40.0, 30.0, 25.0)
                .setWeightKg(6.5)
                .setDeclaredValue(BigDecimal.valueOf(85_000.0))
                .addItem(new OrderItem("SKU-8812", "Industrial Router", 1, BigDecimal.valueOf(65_000.0), 4.5))
                .addItem(new OrderItem("SKU-0044", "Shielded Ethernet Cable 50m", 2, BigDecimal.valueOf(10_000.0), 1.0))
                .build();

        CarrierFactory kazPostFactory = new KazPostCarrierFactory();
        facade.processStandardGroundOrder(order, kazPostFactory);
    }

    // Scenario 2: Heavy-lift fragile server via FedEx + EcoFreight + Fragile & Insurance decorators
    private static void runScenarioTwo(WarehouseFulfillmentFacade facade) {
        System.out.println("[Scenario 2] Heavy Fragile Server Order (FedEx + EcoFreight + Decorators)");

        Address origin = new Address("Kabanbay Batyr 60/1", "Astana", "Astana", "010000", "Kazakhstan");
        Address destination = new Address("Turan Ave 37", "Shymkent", "Turkistan", "160000", "Kazakhstan");

        FulfillmentOrder serverOrder = new FulfillmentOrder.Builder()
                .setOrderId("ORD-102")
                .setCustomerName("KazCloud LLP")
                .setCustomerEmail("ops@kazcloud.kz")
                .setOriginAddress(origin)
                .setDestinationAddress(destination)
                .setPriority(PriorityLevel.EXPEDITED)
                .setDimensionsCm(70.0, 50.0, 30.0)
                .setWeightKg(28.0)
                .setDeclaredValue(BigDecimal.valueOf(1_800_000.0))
                .addItem(new OrderItem("SKU-9901", "Rackmount Server Unit", 1, BigDecimal.valueOf(1_800_000.0), 28.0))
                .build();

        CarrierFactory fedExFactory = new FedExCarrierFactory();
        CostCalculationStrategy ecoStrategy = new EcoFreightCostStrategy();
        facade.processFragileInsuredOrder(serverOrder, fedExFactory, ecoStrategy, 0.015);
    }

    // Scenario 3: Urgent cold-chain medical order via DHL + ExpressAir + 3 decorators
    private static void runScenarioThree(WarehouseFulfillmentFacade facade) {
        System.out.println("[Scenario 3] Urgent Cold-Chain Medical Order (DHL + ExpressAir + 3 Decorators)");

        Address origin = new Address("Al-Farabi Ave 71", "Almaty", "Almaty", "050000", "Kazakhstan");
        Address destination = new Address("Kerei-Zhanibek Khandar 3", "Astana", "Astana", "010000", "Kazakhstan");

        FulfillmentOrder medicalOrder = new FulfillmentOrder.Builder()
                .setOrderId("ORD-103")
                .setCustomerName("National Oncology Center")
                .setCustomerEmail("pharmacy@nroc.kz")
                .setOriginAddress(origin)
                .setDestinationAddress(destination)
                .setPriority(PriorityLevel.CRITICAL_URGENT)
                .setDimensionsCm(45.0, 35.0, 30.0)
                .setWeightKg(8.2)
                .setDeclaredValue(BigDecimal.valueOf(3_200_000.0))
                .addItem(new OrderItem("SKU-0104", "Biologic Vaccine Ampoules", 2, BigDecimal.valueOf(1_600_000.0), 4.1))
                .build();

        CarrierFactory dhlFactory = new DHLCarrierFactory();
        CostCalculationStrategy airStrategy = new ExpressAirCostStrategy();
        facade.processColdChainMedicalOrder(medicalOrder, dhlFactory, airStrategy, "2C to 8C", 0.02);
    }

    // Scenario 4: Builder input validation check
    private static void runValidationTest() {
        System.out.println("[Scenario 4] Builder Validation Check");
        try {
            new FulfillmentOrder.Builder()
                    .setOrderId("ORD-999")
                    .setCustomerName("Test User")
                    .setCustomerEmail("invalid-email")
                    .setOriginAddress(new Address("Street 1", "Astana", "Astana", "010000", "KZ"))
                    .setDestinationAddress(new Address("Street 2", "Almaty", "Almaty", "050000", "KZ"))
                    .setDimensionsCm(10.0, 10.0, 10.0)
                    .setWeightKg(-5.0)
                    .build();
        } catch (IllegalArgumentException e) {
            System.out.println("  Caught expected validation error: " + e.getMessage() + "\n");
        }
    }
}