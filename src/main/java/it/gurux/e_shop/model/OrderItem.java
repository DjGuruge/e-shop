package it.gurux.e_shop.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.query.Order;

import java.math.BigDecimal;

@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity;
    private BigDecimal price;
    @ManyToOne
    @JoinColumn(name =  "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn (name = "product_id")
    private Product product;

    public OrderItem( Order order,int quantity, Product product, BigDecimal price) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.price = price;

    }
}
