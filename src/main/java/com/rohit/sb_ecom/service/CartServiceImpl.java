package com.rohit.sb_ecom.service;

import com.rohit.sb_ecom.exceptions.APIException;
import com.rohit.sb_ecom.exceptions.ResourceNotFoundException;
import com.rohit.sb_ecom.model.Cart;
import com.rohit.sb_ecom.model.CartItem;
import com.rohit.sb_ecom.model.Product;
import com.rohit.sb_ecom.payload.CartDTO;
import com.rohit.sb_ecom.payload.ProductDTO;
import com.rohit.sb_ecom.repositories.CartItemRepository;
import com.rohit.sb_ecom.repositories.CartRepository;
import com.rohit.sb_ecom.repositories.ProductRepository;
import com.rohit.sb_ecom.utils.AuthUtil;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements CartService{

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthUtil authUtil;

    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        Cart cart = createCart();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ProductId", productId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(
                cart.getCartId(),
                productId
        );

        if(cartItem != null){
            throw new APIException("Product " + product.getUser() +" already exists.");
        }

        if(product.getQuantity() == 0){
            throw new APIException(product.getProductName() + " is not available.");
        }

        if(product.getQuantity() < quantity){
            throw new APIException("please make an order of the "+product.getProductName()+" less than or equal of quantity "+product.getQuantity()+".");
        }

        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());

        cart.getCartItems().add(newCartItem);

        cartItemRepository.save(newCartItem);

        cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        List<CartItem> cartItems = cart.getCartItems();

        Stream<ProductDTO> productStream = cartItems.stream().map(item -> {
            ProductDTO map = modelMapper.map(item.getProduct(), ProductDTO.class);
            map.setQuantity(item.getQuantity());
            return map;
        });

        cartDTO.setProducts(productStream.toList());

        return cartDTO;
    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();

        if(carts.size() == 0){
            throw new APIException("No Cart Exists");
        }

        List<CartDTO> cartDTOS = carts.stream()
                .map(cart -> {
                    CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
                    List<ProductDTO> products = cart.getCartItems().stream()
                            .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).toList();
                    cartDTO.setProducts(products);
                    return cartDTO;
                }).toList();


        return cartDTOS;
    }

    @Override
    public CartDTO getCart(String emailId, Long cartId) {
        Cart cart = cartRepository.getCaryByEmailAndCardId(emailId, cartId);

        if(cart == null){
            throw new ResourceNotFoundException("Cart", "cartId", cartId);
        }

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        List<ProductDTO> products = cart.getCartItems().stream()
                .map(item -> {
                    ProductDTO dto = modelMapper.map(item.getProduct(), ProductDTO.class);
                    dto.setQuantity(item.getQuantity());
                    return dto;
                }).toList();

        cartDTO.setProducts(products);

        return cartDTO;
    }

    @Override
    @Transactional
    public CartDTO updateProductQuantityInCart(Long productId, Integer quantity) {

        String email = authUtil.loggedInEmail();
        Cart userCart = cartRepository.getCartByEmail(email);
        Long cartId = userCart.getCartId();


        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ProductId", productId));

        if(product.getQuantity() == 0){
            throw new APIException(product.getProductName() + " is not available.");
        }

        if(product.getQuantity() < quantity){
            throw new APIException("please make an order of the "+product.getProductName()+" less than or equal of quantity "+product.getQuantity()+".");
        }

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);
        if(cartItem == null){
            throw new APIException("Product "+product.getProductName()+" not available in the cart");
        }

        int newQuantity = cartItem.getQuantity() + quantity;

        if(newQuantity < 0){
            throw new APIException("The resulting quantity cannot be negative");
        }

        if(newQuantity == 0){

            deleteProductFromCart(cartId, productId);
        } else {
            cartItem.setProductPrice(product.getSpecialPrice());
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setDiscount(product.getDiscount());
            cart.setTotalPrice(cart.getTotalPrice() + (cartItem.getProductPrice() * quantity));
            cartRepository.save(cart);
        }

        CartItem updatedItem = cartItemRepository.save(cartItem);
        if(updatedItem.getQuantity() == 0){
            cartItemRepository.deleteById(updatedItem.getCartItemId());
        }

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        List<CartItem> cartItems = cart.getCartItems();

        Stream<ProductDTO> productStream = cartItems.stream().map(item -> {
            ProductDTO prd = modelMapper.map(item.getProduct(), ProductDTO.class);
            prd.setQuantity(item.getQuantity());
            return prd;
        });

        cartDTO.setProducts(productStream.toList());

        return cartDTO;
    }

    @Transactional
    @Override
    public String deleteProductFromCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Card","cartId",cartId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);

        if(cartItem == null){
            throw new ResourceNotFoundException("Product","product",productId);
        }

        cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity()));

        cartItemRepository.deleteCartItemByProductIdAndCartId(cartId, productId);

        return "Product " + cartItem.getProduct().getProductName() +" is removed from the cart!!!";
    }

    @Override
    public void updateProductInCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ProductId", productId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);

        if(cartItem == null){
            throw new APIException("Product "+product.getProductName()+" not available in the cart!!!");
        }

        double cartPrice = cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity());

        cartItem.setProductPrice(product.getSpecialPrice());

        cart.setTotalPrice(cartPrice + (cartItem.getProductPrice() * cartItem.getQuantity()));

        cartItem = cartItemRepository.save(cartItem);
    }

    public Cart createCart(){
        Cart userCart = cartRepository.getCartByEmail(authUtil.loggedInEmail());

        if(userCart != null){
            return userCart;
        } else {
            Cart cart = new Cart();
            cart.setTotalPrice(0.00);
            cart.setUser(authUtil.loggedInUser());
            Cart newCart = cartRepository.save(cart);

            return newCart;
        }
    }
}
