package com.smartwarehouse.order.observer;

import com.smartwarehouse.model.OrderStatus;
import com.smartwarehouse.order.builder.FulfillmentOrder;

// Yerassyl - Observer interface for order lifecycle events
public interface OrderObserver {
    void onStatusChanged(
            FulfillmentOrder order,
            OrderStatus previousStatus,
            OrderStatus newStatus,
            String remarks
    );
}