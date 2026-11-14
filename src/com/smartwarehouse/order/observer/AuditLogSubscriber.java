package com.smartwarehouse.order.observer;

import com.smartwarehouse.model.OrderStatus;
import com.smartwarehouse.order.builder.FulfillmentOrder;

// Yerassyl - Observer recording compliance audit entries
public class AuditLogSubscriber implements OrderObserver {

    @Override
    public void onStatusChanged(
            FulfillmentOrder order,
            OrderStatus previousStatus,
            OrderStatus newStatus,
            String remarks
    ) {
        System.out.printf("  [AUDIT LOG] id=%s | from=%s | to=%s | declaredValue=%s KZT%n",
                order.getOrderId(),
                previousStatus,
                newStatus,
                order.getDeclaredValue()
        );
    }
}