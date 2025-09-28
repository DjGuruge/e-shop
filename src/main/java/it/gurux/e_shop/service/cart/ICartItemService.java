package it.gurux.e_shop.service.cart;

import it.gurux.e_shop.model.CartItem;

public interface ICartItemService {
    void addItemToCart(Long CartId,Long productId, int quantity);
    void removeItemFromCart(Long cartId, Long productId);
    void updateItemQuantity(Long CartId,Long productId, int quantity);

}
