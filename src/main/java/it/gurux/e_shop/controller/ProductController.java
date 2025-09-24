package it.gurux.e_shop.controller;

import it.gurux.e_shop.dto.ProductDto;
import it.gurux.e_shop.exception.ResourceNotFoundException;
import it.gurux.e_shop.model.Product;
import it.gurux.e_shop.request.AddProductRequest;
import it.gurux.e_shop.request.ProductUpdateRequest;
import it.gurux.e_shop.response.ApiResponse;
import it.gurux.e_shop.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "${api.prefix}/products")
public class ProductController {


    private final IProductService productService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductDto> convertedProduct = productService.getConvertedProducts(products);
        return ResponseEntity.ok(new ApiResponse("Success", convertedProduct));
    }


    @GetMapping("/product/{productId}/product")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
        try {
            Product product = productService.getProductByID(productId);
            ProductDto productDto = productService.convertToDto(product);
            return ResponseEntity.ok(new ApiResponse("Product Found", productDto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Product not found!", null));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product) {
        try {
            Product theProduct = productService.addProduct(product);
            ProductDto productDto = productService.convertToDto(theProduct);
            return ResponseEntity.ok(new ApiResponse("Add product success", productDto));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }

    }


    @PutMapping("/product/{productId}/update")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductUpdateRequest productRequest
            , @PathVariable Long productId) {
        try {
            Product theProduct = productService.updateProduct(productRequest, productId);
            ProductDto productDto = productService.convertToDto(theProduct);
            return ResponseEntity.ok(new ApiResponse("Success", productDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }


    @DeleteMapping("/product/{productId}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) {
        try {
            productService.deleteProductById(productId);
            return ResponseEntity.ok(new ApiResponse("deleted", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/products/by/brand-and-name")
    public ResponseEntity<ApiResponse> getProductByBrandAndName(@RequestParam String brand, @RequestParam String name) {
        try {
            List<Product> productList = productService.getProductsByBrandAndName(name, brand);

            if (productList.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Product not found!", null));
            }
            List<ProductDto> convertedProduct = productService.getConvertedProducts(productList);
            return ResponseEntity.ok(new ApiResponse("Product Found", convertedProduct));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }
    }


    @GetMapping("/products/by/category-and-brand")
    public ResponseEntity<ApiResponse> getProductByCategoryAndBrand(@RequestParam String nameCategory, @RequestParam String brand){
        try {
            List<Product> productList = productService.getProductsByCategoryAndBrand(nameCategory, brand);
            List<ProductDto> convertedProduct = productService.getConvertedProducts(productList);
            if (productList.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Product not found!", null));
            }
            return ResponseEntity.ok(new ApiResponse("Product Found", convertedProduct));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Not found!",null));
        }
    }

    @GetMapping("/products/{name}/products")
    public ResponseEntity<ApiResponse> getProductByName(@PathVariable String name){
        try {
            List<Product> productList = productService.getProductsByName(name);
            List<ProductDto> convertedProduct = productService.getConvertedProducts(productList);
            if (productList.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Product not found!", null));
            }
            return ResponseEntity.ok(new ApiResponse("Product Found", convertedProduct));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Not found!",null));
        }
    }

    @GetMapping("/product/by-brand")
    public ResponseEntity<ApiResponse> getProductByBrand(@RequestParam String brand){
        try {
            List<Product> productList = productService.getProductsByBrand(brand);
            List<ProductDto> convertedProduct = productService.getConvertedProducts(productList);

            if (productList.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Product not found!", null));
            }
            return ResponseEntity.ok(new ApiResponse("Product Found", convertedProduct));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage(),null));
        }
    }

    @GetMapping("/product/{category}/all/products")
    public ResponseEntity<ApiResponse> getProductsByCategory(@PathVariable String category){
        try {
            List<Product> productList = productService.getProductsByCategory(category);
            List<ProductDto> convertedProduct = productService.getConvertedProducts(productList);

            if (productList.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Product not found!", null));
            }
            return ResponseEntity.ok(new ApiResponse("Product Found", convertedProduct));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage(),null));
        }
    }

    @GetMapping("/product/count/by-brand/and-name")
    public ResponseEntity<ApiResponse> countProductsByBrandAndName(@RequestParam String brand,@RequestParam String name){
        try {
            var productList = productService.countProductsByBrandAndName(brand,name);
             return ResponseEntity.ok(new ApiResponse("Product counted", productList));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage(),null));
        }
    }






}
