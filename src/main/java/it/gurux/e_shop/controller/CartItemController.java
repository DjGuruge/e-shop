package it.gurux.e_shop.controller;

import it.gurux.e_shop.exception.ResourceNotFoundException;
import it.gurux.e_shop.response.ApiResponse;
import it.gurux.e_shop.service.cart.ICartItemService;
import it.gurux.e_shop.service.cart.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/cart/{cartId}/item/{itemId}/update")
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
