package it.gurux.e_shop.service.product;

import it.gurux.e_shop.model.Product;
import it.gurux.e_shop.request.AddProductRequest;

import java.util.List;

//questa classe dovrebbe essere giusta

public interface IProductService {

    Product addProduct(AddProductRequest request);
    Product getProductByID(Long id);
    void deleteProductById(Long id);
    void updateProduct(Product product, Long productId);
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByCategoryAndBrand(String category, String brand);
    List<Product> getProductsByName(String name);
    List<Product> getProductsByBrandAndName(String brand, String name);
    Long countProductsByBrandAndName(String brand, String name);



}
