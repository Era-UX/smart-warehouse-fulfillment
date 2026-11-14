package com.smartwarehouse.order.observer;

import com.smartwarehouse.model.OrderStatus;
import com.smartwarehouse.order.builder.FulfillmentOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// Yerassyl - Concrete Subject broadcasting order status updates
public class OrderTrackingSubject implements OrderSubject {
    private final List<OrderObserver> observers = new ArrayList<>();

    @Override
    public void registerObserver(OrderObserver observer) {
        Objects.requireNonNull(observer, "Observer cannot be null.");
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void unregisterObserver(OrderObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(
            FulfillmentOrder order,
            OrderStatus previousStatus,
            OrderStatus newStatus,
            String remarks
    ) {
        Objects.requireNonNull(order, "Order cannot be null.");
        for (OrderObserver observer : observers) {
            observer.onStatusChanged(order, previousStatus, newStatus, remarks);
        }
    }
}