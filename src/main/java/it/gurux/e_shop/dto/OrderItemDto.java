package it.gurux.e_shop.dto;

import lombok.Data;
import lombok.Setter;

import java.math.BigDecimal;

@Data
public class OrderItemDto {
    private Integer quantity;
    private BigDecimal price;
    private Long productId;
    private String productName;
    private String productBrand;

}
