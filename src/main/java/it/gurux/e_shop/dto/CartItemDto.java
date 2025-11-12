package it.gurux.e_shop.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemDto {

    private Long itemId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private ProductDto productDto;
    private BigDecimal totalPrice;
}
