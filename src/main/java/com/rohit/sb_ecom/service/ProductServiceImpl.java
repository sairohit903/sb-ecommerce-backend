package com.rohit.sb_ecom.service;

import com.rohit.sb_ecom.exceptions.APIException;
import com.rohit.sb_ecom.exceptions.ResourceNotFoundException;
import com.rohit.sb_ecom.model.Cart;
import com.rohit.sb_ecom.model.Category;
import com.rohit.sb_ecom.model.Product;
import com.rohit.sb_ecom.payload.CartDTO;
import com.rohit.sb_ecom.payload.ProductDTO;
import com.rohit.sb_ecom.payload.ProductResponse;
import com.rohit.sb_ecom.repositories.CartRepository;
import com.rohit.sb_ecom.repositories.CategoryRepository;
import com.rohit.sb_ecom.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {
        Product product = modelMapper.map(productDTO, Product.class);
        Product productFromDb = productRepository.findByProductName(product.getProductName());
        if(productFromDb != null){
            throw new APIException("Product with product name " + product.getProductName() + " already exists !!!");
        }
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", categoryId));

        product.setImage("default.png");
        product.setCategory(category);
        Double specialPrice = (product.getPrice() - (product.getDiscount() * 0.01) * product.getPrice());
        product.setSpecialPrice(specialPrice);
        Product savedProduct = productRepository.save(product);

        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder){
        Sort sortByandOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByandOrder);
        Page<Product> productsPage = productRepository.findAll(pageDetails);

        List<Product> products = productsPage.getContent();
        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class)).toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContents(productDTOS);
        productResponse.setPageNumber(productsPage.getNumber());
        productResponse.setPageSize(productsPage.getSize());
        productResponse.setTotalElements(productsPage.getTotalElements());
        productResponse.setTotalPages(productsPage.getTotalPages());
        productResponse.setLastPage(productsPage.isLast());

        return productResponse;
    }

    @Override
    public ProductResponse searchByCategoryId(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", categoryId));

        Sort sortByandOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByandOrder);
        Page<Product> productsPage = productRepository.findByCategoryOrderByPriceAsc(category,pageDetails);

        List<Product> products = productsPage.getContent();

        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class)).toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContents(productDTOS);
        productResponse.setPageNumber(productsPage.getNumber());
        productResponse.setPageSize(productsPage.getSize());
        productResponse.setTotalElements(productsPage.getTotalElements());
        productResponse.setTotalPages(productsPage.getTotalPages());
        productResponse.setLastPage(productsPage.isLast());

        return productResponse;
    }

    @Override
    public ProductResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByandOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByandOrder);
        Page<Product> productsPage = productRepository.findByProductNameLikeIgnoreCase('%'+keyword+'%', pageDetails);

        List<Product> products = productsPage.getContent();

        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class)).toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContents(productDTOS);
        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO,Long productId) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ProductId", productId));

        Product product = modelMapper.map(productDTO, Product.class);
        existingProduct.setProductName(product.getProductName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setDiscount(product.getDiscount());
        Double specialPrice = (product.getPrice() - (product.getDiscount() * 0.01) * product.getPrice());
        existingProduct.setSpecialPrice(specialPrice);

        productRepository.save(existingProduct);

        List<Cart> carts = cartRepository.findCartByProductId(productId);

        List<CartDTO> cartDTOS = carts.stream().map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
            List<ProductDTO> products = cart.getCartItems().stream()
                    .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).toList();
            cartDTO.setProducts(products);
            return cartDTO;
        }).toList();

        cartDTOS.forEach(cart -> cartService.updateProductInCart(cart.getCartId(), productId));

        return modelMapper.map(existingProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ProductId", productId));

        productRepository.delete(existingProduct);

        List<Cart> carts = cartRepository.findCartByProductId(productId);

        carts.forEach(cart -> cartService.deleteProductFromCart(cart.getCartId(), productId));

        return modelMapper.map(existingProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        Product productFromDB = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ProductId", productId));


        String fileName = fileService.uploadImage(path, image);

        productFromDB.setImage(fileName);

        Product updatedProduct = productRepository.save(productFromDB);

        return modelMapper.map(updatedProduct, ProductDTO.class);
    }

}
