package com.smartwarehouse.order.observer;

import com.smartwarehouse.model.OrderStatus;
import com.smartwarehouse.order.builder.FulfillmentOrder;

// Yerassyl - Subject interface for managing status subscribers
public interface OrderSubject {
    void registerObserver(OrderObserver observer);
    void unregisterObserver(OrderObserver observer);
    void notifyObservers(
            FulfillmentOrder order,
            OrderStatus previousStatus,
            OrderStatus newStatus,
            String remarks
    );
}