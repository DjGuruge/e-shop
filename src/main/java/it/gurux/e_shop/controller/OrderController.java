package it.gurux.e_shop.controller;

import it.gurux.e_shop.model.Order;
import it.gurux.e_shop.response.ApiResponse;
import it.gurux.e_shop.service.order.IOrderService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {

    private final IOrderService orderService;

    public ResponseEntity<ApiResponse> createOrder(Long userId){
        Order order = orderService.placeOrder(userId);
        return ResponseEntity.ok(new ApiResponse("Order Success! ",order));
    }

    public ResponseEntity<ApiResponse>
}
