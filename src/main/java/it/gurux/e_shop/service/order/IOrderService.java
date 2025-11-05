package it.gurux.e_shop.service.order;

import it.gurux.e_shop.model.Order;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userdId);
    Order getOrder(Long orderId);

    List<Order> getUserOrders(Long userId);
}
