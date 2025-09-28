package it.gurux.e_shop.service.cart;

import it.gurux.e_shop.model.Cart;
import it.gurux.e_shop.model.CartItem;
import it.gurux.e_shop.model.Product;
import it.gurux.e_shop.repository.CartItemRepository;
import it.gurux.e_shop.service.product.IProductService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@NoArgsConstructor
public class CartItemService implements ICartItemService{
    private final CartItemRepository cartItemRepository;
    private final IProductService productService;
    private final ICartService cartService;


    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        //1. Get the cart
        //2. get the product
        //3. check if the product is already in the cart
        //4. if yes increase the quantity with the requested quantity
        //5. if not, initiate a new cartItem entry
        Cart cart = cartService.getCart(cartId);
        Product product = productService.getProductByID(productId);
        CartItem cartItem = cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().orElse(new CartItem());
        if (cartItem.getId() == null){
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);

        }

    }

    @Override
    public void removeItemFromCart(Long cartId, Long productId) {

    }

    @Override
    public void updateItemQuantity(Long CartId, Long productId, int quantity) {

    }
}
