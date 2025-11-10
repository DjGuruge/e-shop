package it.gurux.e_shop.dto;

import lombok.Data;
import lombok.Setter;

import java.math.BigDecimal;

@Data
public class OrderItemDto {
    private Long productId;
    private String productName;
    private int quantity;
    private BigDecimal price;
}
