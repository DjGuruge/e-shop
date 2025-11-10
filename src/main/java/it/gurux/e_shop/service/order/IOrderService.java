package it.gurux.e_shop.service.order;

import it.gurux.e_shop.dto.OrderDto;
import it.gurux.e_shop.model.Order;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);
    OrderDto getOrder(Long orderId);

    List<OrderDto> getUserOrders(Long userId);
}
