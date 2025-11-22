package it.gurux.e_shop.service.cart;

import it.gurux.e_shop.exception.ResourceNotFoundException;
import it.gurux.e_shop.model.Cart;
import it.gurux.e_shop.model.User;
import it.gurux.e_shop.repository.CartItemRepository;
import it.gurux.e_shop.repository.CartRepository;
import it.gurux.e_shop.repository.UserRepository;
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
    private final UserRepository userRepository;

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
    @Transactional
    public Long initializeNewCart(){
        Cart newCart = new Cart();
        Cart savedCart = cartRepository.save(newCart);
        log.info("Created anonymus cart with ID : {}", savedCart.getId());
        return savedCart.getId();
    }


    @Override
    @Transactional
    public Long initializeNewCart(Long userId){
        if (userId == null){
            return initializeNewCart();
        }
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));
        Cart existingCart = cartRepository.findByUserId(userId);
        if (existingCart != null){
            log.info(" User {} already has a cart with id : {}", userId, existingCart.getId());
            return existingCart.getId();
        }
        Cart newCart = new Cart();
        newCart.setUser(user);
        log.info("Creating a  new cart...");
        Cart savedCart = cartRepository.save(newCart);
        log.info("Created a cart with iD:{}, version {}, and user id : {}",savedCart.getId(),savedCart.getVersion(),userId);
        return savedCart.getId();

        //        Long newCartId = cartIdGenerator.incrementAndGet();
//        newCart.setId(newCartId);

    }

    @Override
    @Transactional
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public Cart getOrCreateCartForUser(Long userId){
        Cart cart = cartRepository.findByUserId(userId);

        if ( cart == null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException(" USer not found "));
            cart = new Cart();
            cart.setUser(user);
            cart = cartRepository.save(cart);
            log.info("Create new cart Id : {} for user : {}", cart.getId(), userId);
        }else{
            log.info(" Found existing cart with ID : {} for user : {}",cart.getId(),userId);
        }
        return cart;
    }

    @PostConstruct
    public void init() {
        // Initialize with the max ID from database
        Long maxId = cartRepository.findMaxId().orElse(0L);
        cartIdGenerator.set(maxId);
    }


}
