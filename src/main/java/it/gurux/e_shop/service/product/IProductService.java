package it.gurux.e_shop.service.product;

import java.util.List;

import it.gurux.e_shop.dto.ProductDto;
import it.gurux.e_shop.model.Product;
import it.gurux.e_shop.request.AddProductRequest;
import it.gurux.e_shop.request.ProductUpdateRequest;

//questa classe dovrebbe essere giusta

public interface IProductService {

    Product addProduct(AddProductRequest product);
    Product getProductByID(Long id);
    void deleteProductById(Long id);
    Product updateProduct(ProductUpdateRequest product, Long productId);
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByCategoryAndBrand(String category, String brand);
    List<Product> getProductsByName(String name);
    List<Product> getProductsByBrandAndName(String brand, String name);
    Long countProductsByBrandAndName(String brand, String name);


    List<ProductDto> getConvertedProducts(List<Product> products);

    ProductDto convertToDto(Product product);
}
