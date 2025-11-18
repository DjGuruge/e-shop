package it.gurux.e_shop.controller;

import it.gurux.e_shop.exception.ResourceNotFoundException;
import it.gurux.e_shop.response.ApiResponse;
import it.gurux.e_shop.service.cart.ICartItemService;
import it.gurux.e_shop.service.cart.ICartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {

    private final ICartItemService cartItemService;
    private final ICartService cartService;


    @PostMapping("/item/add")
    public ResponseEntity<ApiResponse> addItem(@RequestParam(required = false) Long cartId,
                                               @RequestParam Long productId,
                                               @RequestParam Integer quantity) {
        int maxRetries = 3;
        int attempt = 0;
        while (attempt < maxRetries) {
            try {
                if (cartId == null) {
                    log.info("No cart id provide, creating a new one");
                    cartId = cartService.initializeNewCart();
                    log.info("created a new cart with id :{}", cartId);
                }
                cartItemService.addItemToCart(cartId, productId, quantity);
                return ResponseEntity.ok(new ApiResponse("Item added to Cart Successfully", null));
            } catch (OptimisticLockingFailureException e) {
                attempt++;
                log.warn("otimistic locking faillure on attempt {} for cartId : {}",
                        attempt, cartId, e.getMessage());
                if (attempt >= maxRetries) {
                    log.error("Max retries exceeded for adding items to cart", e);
                    return ResponseEntity.status(CONFLICT)
                            .body(new ApiResponse("Unable to add item, Try again.", null));
                }
                try {
                    Thread.sleep(50L * attempt);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return ResponseEntity.status(CONFLICT)
                            .body(new ApiResponse("Operation interrupted.Try gain.", null));
                }
                if (attempt == 1) {
                    cartId = null;
                }

            } catch (ResourceNotFoundException e) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
            }
        }
        return ResponseEntity.status(CONFLICT)
                .body(new ApiResponse("Unable to add item. try again .", null));
    }
    @DeleteMapping("/{cartId}/item/{itemId}/remove")
    public ResponseEntity<ApiResponse> removeItemFromCart (@PathVariable Long cartId,
                                                           @PathVariable Long itemId){
        try {
            cartItemService.removeItemFromCart(cartId, itemId);
            return  ResponseEntity.ok(new ApiResponse( ("Item Removed Success"), null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/cart/{cartId}/item/{itemId}/update")
    public ResponseEntity<ApiResponse> updateItemQuantity(@PathVariable Long cartId
            ,@PathVariable Long itemId
            ,@RequestParam Integer quantity ){
        try {
            cartItemService.updateItemQuantity(cartId, itemId, quantity);
            return ResponseEntity.ok(new ApiResponse("Item quantity updated",null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }

    }

}
