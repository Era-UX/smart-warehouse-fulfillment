package com.smartwarehouse.facade;

import com.smartwarehouse.carrier.factory.Carrier;
import com.smartwarehouse.carrier.factory.CarrierFactory;
import com.smartwarehouse.carrier.strategy.CostCalculationStrategy;
import com.smartwarehouse.carrier.strategy.StandardGroundCostStrategy;
import com.smartwarehouse.model.OrderStatus;
import com.smartwarehouse.order.builder.FulfillmentOrder;
import com.smartwarehouse.order.observer.AuditLogSubscriber;
import com.smartwarehouse.order.observer.CustomerNotificationSubscriber;
import com.smartwarehouse.order.observer.OrderTrackingSubject;
import com.smartwarehouse.order.observer.WarehouseDashboardNotifier;
import com.smartwarehouse.packaging.decorator.ColdChainThermalDecorator;
import com.smartwarehouse.packaging.decorator.FragileHandlingDecorator;
import com.smartwarehouse.packaging.decorator.FulfillablePackage;
import com.smartwarehouse.packaging.decorator.InsuranceCoverageDecorator;
import com.smartwarehouse.packaging.decorator.StandardOrderPackage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

/**
 * Zhanibek - Facade Pattern.
 * Coordinates order inspection, decorator assembly, tariff calculation,
 * carrier creation, and observer notifications behind a single interface.
 */
public class WarehouseFulfillmentFacade {
    private final OrderTrackingSubject trackingSubject;

    public WarehouseFulfillmentFacade(OrderTrackingSubject trackingSubject) {
        this.trackingSubject = Objects.requireNonNull(trackingSubject, "Tracking subject cannot be null.");
    }

    public static WarehouseFulfillmentFacade createDefaultFacade() {
        OrderTrackingSubject subject = new OrderTrackingSubject();
        subject.registerObserver(new WarehouseDashboardNotifier());
        subject.registerObserver(new CustomerNotificationSubscriber());
        subject.registerObserver(new AuditLogSubscriber());
        return new WarehouseFulfillmentFacade(subject);
    }

    public void processStandardGroundOrder(FulfillmentOrder order, CarrierFactory carrierFactory) {
        processOrder(order, carrierFactory, new StandardGroundCostStrategy(), false, 0.0, null);
    }

    public void processFragileInsuredOrder(
            FulfillmentOrder order,
            CarrierFactory carrierFactory,
            CostCalculationStrategy costStrategy,
            double insuranceRate
    ) {
        processOrder(order, carrierFactory, costStrategy, true, insuranceRate, null);
    }

    public void processColdChainMedicalOrder(
            FulfillmentOrder order,
            CarrierFactory carrierFactory,
            CostCalculationStrategy costStrategy,
            String temperatureRange,
            double insuranceRate
    ) {
        processOrder(order, carrierFactory, costStrategy, true, insuranceRate, temperatureRange);
    }

    public void processOrder(
            FulfillmentOrder order,
            CarrierFactory carrierFactory,
            CostCalculationStrategy costStrategy,
            boolean fragile,
            double insuranceRate,
            String coldChainRange
    ) {
        Objects.requireNonNull(order, "Order cannot be null.");
        Objects.requireNonNull(carrierFactory, "CarrierFactory cannot be null.");
        Objects.requireNonNull(costStrategy, "CostCalculationStrategy cannot be null.");

        try {
            runWarehouseSafetyChecks(order);

            trackingSubject.notifyObservers(
                    order,
                    OrderStatus.CREATED,
                    OrderStatus.INSPECTED_AND_PACKED,
                    "Items verified and packed"
            );

            FulfillablePackage pkg = buildDecoratedPackage(order, fragile, insuranceRate, coldChainRange);

            double finalWeight = pkg.getTotalWeight();
            BigDecimal shippingCost = costStrategy.computeShippingCost(order, finalWeight);
            BigDecimal handlingFee = pkg.getHandlingFee().setScale(2, RoundingMode.HALF_UP);
            BigDecimal totalCost = shippingCost.add(handlingFee);

            Carrier carrier = carrierFactory.createCarrier();
            trackingSubject.notifyObservers(
                    order,
                    OrderStatus.INSPECTED_AND_PACKED,
                    OrderStatus.CARRIER_ASSIGNED,
                    "Assigned to " + carrier.getCarrierName()
            );

            String notes = String.join("; ", pkg.getHandlingInstructions());
            String trackingNumber = carrier.dispatchConsignment(order, notes);

            trackingSubject.notifyObservers(
                    order,
                    OrderStatus.CARRIER_ASSIGNED,
                    OrderStatus.IN_TRANSIT,
                    "Handed over with tracking " + trackingNumber
            );

            printDispatchSummary(order, carrier, trackingNumber, finalWeight, shippingCost, handlingFee, totalCost, pkg.getCareTags());

        } catch (Exception e) {
            trackingSubject.notifyObservers(
                    order,
                    OrderStatus.INSPECTED_AND_PACKED,
                    OrderStatus.EXCEPTION_ENCOUNTERED,
                    "Fulfillment failed: " + e.getMessage()
            );
            throw new RuntimeException("Failed to process order " + order.getOrderId(), e);
        }
    }

    private void runWarehouseSafetyChecks(FulfillmentOrder order) {
        if (order.hasHeavyLiftRequirement()) {
            System.out.println("  [WAREHOUSE NOTE] Heavy cargo (>=25 kg) - forklift handling assigned.");
        }
        if (order.isHighValueCargo()) {
            System.out.println("  [WAREHOUSE NOTE] High-value cargo (>=500,000 KZT) - security cage verification enabled.");
        }
    }

    private FulfillablePackage buildDecoratedPackage(
            FulfillmentOrder order,
            boolean fragile,
            double insuranceRate,
            String coldChainRange
    ) {
        FulfillablePackage pkg = new StandardOrderPackage(order);
        if (fragile) {
            pkg = new FragileHandlingDecorator(pkg);
        }
        if (insuranceRate > 0.0) {
            pkg = new InsuranceCoverageDecorator(pkg, insuranceRate);
        }
        if (coldChainRange != null && !coldChainRange.isBlank()) {
            pkg = new ColdChainThermalDecorator(pkg, coldChainRange);
        }
        return pkg;
    }

    private void printDispatchSummary(
            FulfillmentOrder order,
            Carrier carrier,
            String trackingNumber,
            double finalWeight,
            BigDecimal shippingCost,
            BigDecimal handlingFee,
            BigDecimal totalCost,
            List<String> careTags
    ) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("Dispatch Receipt (" + order.getOrderId() + ") ");
        System.out.println("  Carrier       : " + carrier.getCarrierName());
        System.out.println("  Tracking Code : " + trackingNumber);
        System.out.printf("  Final Weight  : %.2f kg%n", finalWeight);
        System.out.println("  Shipping Cost : " + shippingCost + " KZT");
        System.out.println("  Handling Fee  : " + handlingFee + " KZT");
        System.out.println("  Total Cost    : " + totalCost + " KZT");
        System.out.println("  Care Tags     : " + careTags);
        System.out.println("  Dispatched At : " + timestamp);
        System.out.println("\n");
    }
}