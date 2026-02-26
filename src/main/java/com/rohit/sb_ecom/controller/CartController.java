package com.rohit.sb_ecom.controller;

import com.rohit.sb_ecom.model.Cart;
import com.rohit.sb_ecom.payload.CartDTO;
import com.rohit.sb_ecom.payload.ProductDTO;
import com.rohit.sb_ecom.repositories.CartRepository;
import com.rohit.sb_ecom.service.CartService;
import com.rohit.sb_ecom.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private CartService cartService;

    @PostMapping("/cart/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long productId,
                                                    @PathVariable Integer quantity){
        CartDTO cartDTO = cartService.addProductToCart(productId, quantity);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartDTO);
    }

    @GetMapping("/carts")
    public ResponseEntity<List<CartDTO>> getCarts(){
        List<CartDTO> cartDTOS = cartService.getAllCarts();
        return ResponseEntity.status(HttpStatus.FOUND).body(cartDTOS);
    }

    @GetMapping("/carts/users/cart")
    public ResponseEntity<CartDTO> getCartById(){
        String emailId = authUtil.loggedInEmail();
        Cart cart = cartRepository.getCartByEmail(emailId);
        CartDTO cartDTO = cartService.getCart(emailId, cart.getCartId());
        return ResponseEntity.status(HttpStatus.OK).body(cartDTO);
    }

    @PutMapping("/cart/products/{productId}/quantity/{operation}")
    public ResponseEntity<CartDTO> updateCartProduct(@PathVariable Long productId,
                                                     @PathVariable String operation){
        CartDTO cartDTO = cartService.updateProductQuantityInCart(productId, operation
                .equalsIgnoreCase("delete") ? -1 : 1);

        return ResponseEntity.status(HttpStatus.OK).body(cartDTO);
    }

    @DeleteMapping("/carts/{cartId}/product/{productId}")
    public ResponseEntity<String> deleteProductFromCart(@PathVariable Long cartId,
                                                        @PathVariable Long productId){
        String status = cartService.deleteProductFromCart(cartId, productId);

        return ResponseEntity.status(HttpStatus.OK).body(status);
    }
}
