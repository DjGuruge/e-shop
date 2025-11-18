package it.gurux.e_shop.service.cart;

import it.gurux.e_shop.exception.ResourceNotFoundException;
import it.gurux.e_shop.model.Cart;
import it.gurux.e_shop.repository.CartItemRepository;
import it.gurux.e_shop.repository.CartRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService implements ICartService{

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final AtomicLong cartIdGenerator = new AtomicLong(0);

    @Transactional
    @Override
    public Cart getCart(Long id) {
            // Just return the cart
            return cartRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        }
//        Cart cart = cartRepository.findById(id)
//                .orElseThrow(()-> new ResourceNotFoundException("Cart not found"));
//        BigDecimal totalAmount = cart.getTotalAmount();
//        cart.setTotalAmount(totalAmount);
//        return cartRepository.save(cart);


    @Transactional
    @Override
    public void clearCart(Long id) {
        Cart cart = getCart(id);
        cartItemRepository.deleteAllByCartId(id);
        cart.getItems().clear();
        cartRepository.deleteById(id);

    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id);
        return cart.getTotalAmount();
    }

    @Override
    public Long initializeNewCart(){
        Cart newCart = new Cart();
//        Long newCartId = cartIdGenerator.incrementAndGet();
//        newCart.setId(newCartId);
        log.info("Creating a  new cart...");
        Cart savedCart = cartRepository.save(newCart);
        log.info("Created a cart with iD:{}, version {}",savedCart.getId(),savedCart.getVersion());
        return savedCart.getId();
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }
    @PostConstruct
    public void init() {
        // Initialize with the max ID from database
        Long maxId = cartRepository.findMaxId().orElse(0L);
        cartIdGenerator.set(maxId);
    }


}
