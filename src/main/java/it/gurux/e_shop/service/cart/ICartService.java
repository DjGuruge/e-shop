package it.gurux.e_shop.service.cart;

import it.gurux.e_shop.model.Cart;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Optional;

public interface ICartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);

    Long initializeNewCart();
    Long initializeNewCart(Long userId);

    Cart getCartByUserId(Long userId);
    Cart getOrCreateCartForUser(Long userId);
}
