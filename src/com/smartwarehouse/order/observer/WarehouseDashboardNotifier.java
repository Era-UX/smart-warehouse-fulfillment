package com.smartwarehouse.order.observer;

import com.smartwarehouse.model.OrderStatus;
import com.smartwarehouse.order.builder.FulfillmentOrder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Yerassyl - Observer updating the warehouse operator monitor
public class WarehouseDashboardNotifier implements OrderObserver {
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public void onStatusChanged(
            FulfillmentOrder order,
            OrderStatus previousStatus,
            OrderStatus newStatus,
            String remarks
    ) {
        String time = LocalDateTime.now().format(TIME_FORMAT);
        System.out.printf("[DASHBOARD %s] Order %s: %s -> %s (%s)%n",
                time,
                order.getOrderId(),
                previousStatus,
                newStatus,
                remarks
        );
    }
}