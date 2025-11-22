package it.gurux.e_shop.service.order;

import it.gurux.e_shop.dto.OrderDto;
import it.gurux.e_shop.dto.OrderItemDto;
import it.gurux.e_shop.enums.OrderStatus;
import it.gurux.e_shop.exception.ResourceNotFoundException;
import it.gurux.e_shop.model.Cart;
import it.gurux.e_shop.model.Order;
import it.gurux.e_shop.model.OrderItem;
import it.gurux.e_shop.model.Product;
import it.gurux.e_shop.repository.OrderRepository;
import it.gurux.e_shop.repository.ProductRepository;
import it.gurux.e_shop.service.cart.CartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getOrCreateCartForUser(userId);


        if (cart.getItems() == null || cart.getItems().isEmpty()){
            throw  new ResourceNotFoundException(" Cannot place order : Cart is empty");
        }
        Order order = createOrder(cart);
        List<OrderItem> orderItemList = createOrderItem(order,cart);
        order.setOrderItem(new HashSet<>(orderItemList));
        order.setTotalAmount(calculateTotalAmount(orderItemList));
        Order savedOrder = orderRepository.save(order);
        orderRepository.flush();
        cartService.clearCart(cart.getId());

        return orderRepository.findById(savedOrder.getId())
                .orElseThrow(()-> new ResourceNotFoundException("Order not found"));
    }

    private Order createOrder(Cart cart){
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }

    @Override
    public OrderDto getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .map(this::convertToDto)
                .orElseThrow(()->new ResourceNotFoundException("No Order found"));
    }

    private List<OrderItem> createOrderItem(Order order, Cart cart){
        return cart.getItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();

            if (product.getInventory() < cartItem.getQuantity()){
                throw new ResourceNotFoundException(
                        "Insufficient inventory for product :" + product.getName()
                );
            }

            product.setInventory(product.getInventory() - cartItem.getQuantity());
            productRepository.save(product);
            return new OrderItem(
                    order,
                    product,
                    cartItem.getQuantity(),
                    cartItem.getUnitPrice());
        }).toList();
    }


    private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList) {
        return orderItemList.stream()
                .map(orderItem -> orderItem.getPrice()
                        .multiply(new BigDecimal(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    @Override
    public List<OrderDto> getUserOrders(Long userId){
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(this :: convertToDto).toList();
    }

    //@Override
    private OrderDto convertToDto(Order order){
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setUserId(order.getUser() != null ? order.getUser().getId() : null);
        orderDto.setOrderDate(order.getOrderDate());
        orderDto.setTotalAmount(order.getTotalAmount());
        orderDto.setOrderStatus(order.getOrderStatus());


        if (order.getOrderItem() != null ) {
            List<OrderItemDto> itemDtos = order.getOrderItem().stream().map(this::convertItemToDto)
                    .collect(Collectors.toList());
            orderDto.setItems(itemDtos);
        }
        return orderDto;
    }

    private OrderItemDto convertItemToDto ( OrderItem item) {
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setProductId(item.getId());
        orderItemDto.setQuantity(item.getQuantity());
        orderItemDto.setPrice(item.getPrice());

        if (item.getProduct() != null) {
            orderItemDto.setProductId(item.getProduct().getId());
            orderItemDto.setProductName(item.getProduct().getName());
            orderItemDto.setProductName(item.getProduct().getBrand());
        }

        return orderItemDto;
    }

}
