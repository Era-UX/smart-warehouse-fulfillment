package com.smartwarehouse.order.observer;

import com.smartwarehouse.model.OrderStatus;
import com.smartwarehouse.order.builder.FulfillmentOrder;

// Yerassyl - Observer sending customer email alerts
public class CustomerNotificationSubscriber implements OrderObserver {

    @Override
    public void onStatusChanged(
            FulfillmentOrder order,
            OrderStatus previousStatus,
            OrderStatus newStatus,
            String remarks
    ) {
        System.out.println("  [EMAIL -> " + order.getCustomerEmail() + "] Hello "
                + order.getCustomerName() + ", your order " + order.getOrderId()
                + " status is now: " + newStatus);
    }
}