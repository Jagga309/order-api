package com.app.orderapi.enums;

public enum OrderStatus {
    ORDER_PLACED,ORDER_RECEIVED,PRODUCT_PICKING,PRODUCT_PACKING,
    ORDER_SHIPPING,ORDER_DELIVERED,ORDER_CLOSED;

    public OrderStatus next(){
        OrderStatus[] statuses = OrderStatus.values();
        if(this.ordinal() != OrderStatus.ORDER_CLOSED.ordinal())
        {
            int next = (this.ordinal()+1) % statuses.length;
            return statuses[next];
        }else return statuses[6];
    }
}
