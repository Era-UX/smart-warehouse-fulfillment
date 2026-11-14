# Smart Warehouse Fulfillment & Logistics System
Software Design Patterns — Final Team Project (Astana IT University, Java 17+)

## Overview
This project models an automated warehouse fulfillment and carrier dispatch workflow in Java 17. Instead of treating design patterns as disconnected examples, all six patterns interact in a single pipeline: orders are validated and built, decorated with physical handling requirements, priced dynamically, assigned to a logistics carrier, and tracked across lifecycle observers through a unified facade.

## Team Workload & Pattern Allocation

| Team Member | Design Patterns | Subsystem & Classes |
| :--- | :--- | :--- |
| Yerassyl Aldebayev | Builder, Observer | FulfillmentOrder, OrderSubject, OrderTrackingSubject, OrderObserver, WarehouseDashboardNotifier, CustomerNotificationSubscriber, AuditLogSubscriber, model records/enums |
| Temirlan Tatken | Factory Method, Strategy | Carrier, CarrierFactory, KazPostCarrier, DHLCarrier, FedExCarrier (and their factories), CostCalculationStrategy, StandardGroundCostStrategy, ExpressAirCostStrategy, EcoFreightCostStrategy |
| Zhanibek Abilkhan | Decorator, Facade | FulfillablePackage, StandardOrderPackage, PackageDecorator, FragileHandlingDecorator, InsuranceCoverageDecorator, ColdChainThermalDecorator, WarehouseFulfillmentFacade |

## How the 6 Patterns Work Together

1. Builder Pattern (Yerassyl): FulfillmentOrder.Builder constructs immutable shipment orders step by step and rejects invalid inputs (negative weight, missing items, invalid email) using IllegalArgumentException.
2. Observer Pattern (Yerassyl): OrderTrackingSubject notifies WarehouseDashboardNotifier, CustomerNotificationSubscriber, and AuditLogSubscriber whenever an order moves from CREATED to INSPECTED_AND_PACKED, CARRIER_ASSIGNED, and IN_TRANSIT.
3. Decorator Pattern (Zhanibek): StandardOrderPackage is wrapped at runtime with FragileHandlingDecorator, InsuranceCoverageDecorator, and ColdChainThermalDecorator to stack extra packaging weight, handling fees, and care instructions without subclass explosion.
4. Strategy Pattern (Temirlan): CostCalculationStrategy calculates transit tariffs dynamically using StandardGroundCostStrategy, ExpressAirCostStrategy (accounting for dimensional weight), or EcoFreightCostStrategy.
5. Factory Method Pattern (Temirlan): CarrierFactory subclasses (KazPostCarrierFactory, DHLCarrierFactory, FedExCarrierFactory) instantiate the appropriate Carrier implementation without coupling the fulfillment pipeline to specific courier classes.
6. Facade Pattern (Zhanibek): WarehouseFulfillmentFacade coordinates safety checks, observer updates, package decoration, shipping cost calculation, and carrier dispatch behind simple methods such as processStandardGroundOrder and processColdChainMedicalOrder.

## Clean Code Principles Applied (Robert C. Martin)

1. Meaningful and Intention-Revealing Names & Named Constants (Chapter 2 & Chapter 17, Rule G25):
   - Measurement units are explicit in variable names (weightKg, billableWeightKg, COOLANT_WEIGHT_KG, CUSHION_WEIGHT_KG).
   - Raw numbers are replaced with domain constants in FulfillmentOrder (DIMENSIONAL_WEIGHT_DIVISOR, HEAVY_LIFT_LIMIT_KG, HIGH_VALUE_LIMIT_KZT).
2. Small Functions Doing One Thing (Chapter 3):
   - FulfillmentOrder.Builder.build() delegates validation to focused private helpers: validateDetails(), validateDimensions(), and validateItems().
   - WarehouseFulfillmentFacade.processOrder() delegates steps to runWarehouseSafetyChecks(), buildDecoratedPackage(), and printDispatchSummary().
   - Concrete tariff strategies (StandardGroundCostStrategy, ExpressAirCostStrategy, EcoFreightCostStrategy) isolate each pricing formula in a concise method.
3. Explanatory Code Over Comments (Chapter 4 & Chapter 17, Rule G28):
   - Business rules are encapsulated in self-describing boolean methods such as FulfillmentOrder.hasHeavyLiftRequirement() and FulfillmentOrder.isHighValueCargo() instead of inline comments.
   - Volumetric weight math is encapsulated inside FulfillmentOrder.getDimensionalWeight() and called directly by ExpressAirCostStrategy.
4. Clean Error Handling & Fail-Fast Validation (Chapter 7):
   - Invalid inputs are rejected immediately at construction boundaries using IllegalArgumentException and Objects.requireNonNull(...) in FulfillmentOrder.Builder, Address, OrderItem, InsuranceCoverageDecorator, and ColdChainThermalDecorator.
   - Runtime failures in WarehouseFulfillmentFacade.processOrder() notify observers with OrderStatus.EXCEPTION_ENCOUNTERED before rethrowing a descriptive RuntimeException.
5. Dependency Injection & Programming to Interfaces (Chapters 10 & 11):
   - WarehouseFulfillmentFacade receives OrderTrackingSubject via constructor injection and depends on CarrierFactory and CostCalculationStrategy abstractions rather than concrete courier or pricing classes.
   - PackageDecorator wraps the FulfillablePackage interface, enabling arbitrary decorator combinations at runtime.
6. Immutability & Encapsulation (Chapters 3, 6 & 13):
   - FulfillmentOrder is final with private final fields, no setters; Address and OrderItem are immutable Java 17 records.

## How to Run

### IntelliJ IDEA
1. Open the smart-warehouse-logistics folder in IntelliJ IDEA.
2. Ensure Project SDK is set to Java 17 or higher (File -> Project Structure -> Project).
3. Right-click the src folder and select Mark Directory as -> Sources Root.
4. Open src/com/smartwarehouse/Main.java and run Main.main().

### Command Line
cd smart-warehouse-logistics
javac -d out $(find src -name "*.java")
java -cp out com.smartwarehouse.Main