package it.gurux.e_shop.dto;

import it.gurux.e_shop.model.CartItem;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class CartDto {
    private Long id;
    private Set<CartItemDto> items;
    private BigDecimal totalAmount = BigDecimal.ZERO;

}
