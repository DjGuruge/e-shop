package it.gurux.e_shop.controller;

import it.gurux.e_shop.exception.ResourceNotFoundException;
import it.gurux.e_shop.response.ApiResponse;
import it.gurux.e_shop.service.cart.ICartItemService;
import it.gurux.e_shop.service.cart.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("{$api.prefix}/cartItems")
public class CartItemController {

    private final ICartItemService cartItemService;


    @PostMapping("/item/add")
    public ResponseEntity<ApiResponse> addItem(@RequestParam Long cartId,
                                               @RequestParam Long productId,
                                               @RequestParam Integer quantity){
        try {
            cartItemService.addItemToCart(cartId, productId, quantity);
            return ResponseEntity.ok(new ApiResponse("Item added to Cart Successfully",null));
        } catch (ResourceNotFoundException e) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

}
