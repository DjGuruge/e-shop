package it.gurux.e_shop.model;

import java.math.BigDecimal;

public class CartItem {
    private Long id;
    private Product product;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalprice;
    private Cart cart;
}
