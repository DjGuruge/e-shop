package it.gurux.e_shop.service.cart;

import it.gurux.e_shop.exception.ResourceNotFoundException;
import it.gurux.e_shop.model.Cart;
import it.gurux.e_shop.model.CartItem;
import it.gurux.e_shop.model.Product;
import it.gurux.e_shop.repository.CartItemRepository;
import it.gurux.e_shop.repository.CartRepository;
import it.gurux.e_shop.service.product.IProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor

public class CartItemService implements ICartItemService{
    private final CartItemRepository cartItemRepository;
    private final IProductService productService;
    private final ICartService cartService;
    private final CartRepository cartRepository;


    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        //1. Get the cart
        //2. get the product
        //3. check if the product is already in the cart
        //4. if yes increase the quantity with the requested quantity
        //5. if not, initiate a new cartItem entry
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(()-> new ResourceNotFoundException("cart not found"));
        Product product = productService.getProductByID(productId);

        CartItem existingItem = cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existingItem == null){
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setUnitPrice(product.getPrice());
            newItem.setTotalPrice();

            cart.addItem(newItem);
            log.info("Added a new item to cart. Product {}, Quantity{}",productId,quantity);
        }else{
            existingItem.setQuantity(existingItem.getQuantity()+ quantity);
            existingItem.setTotalPrice();

            cart.getItems().forEach(item->{
                    if (item.getId().equals(existingItem.getId())){
                        item.setQuantity(existingItem.getQuantity());

                    }
            });
                log.info("Udated existing item in cart . Product {}, New quantity {},",
                        productId,existingItem.getQuantity());
        }
//        cartItem.setTotalPrice();
//        cart.addItem(cartItem);
//        cartItemRepository.save(cartItem);
            BigDecimal totalAmount = cart.getItems().stream().map(CartItem::getTotalPrice).reduce(BigDecimal.ZERO,BigDecimal::add);
          cart.setTotalAmount(totalAmount);

        cartRepository.save(cart);

    }

    public CartItem getCartItem(Long cartId, Long productId){
        Cart cart = cartService.getCart(cartId);
        return cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().orElseThrow(() -> new ResourceNotFoundException("Item not found"));

    }

    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        CartItem itemToRemove = getCartItem(cartId, productId);
        cart.removeItem(itemToRemove);
        cartRepository.save(cart);
    }
    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        cart.getItems().stream().filter(item->item.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(item->{
                    item.setQuantity(quantity);
                    item.setUnitPrice(item.getProduct().getPrice());
                    item.setTotalPrice();
                });
        BigDecimal totalAmount = cart.getItems().stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);

    }
}
