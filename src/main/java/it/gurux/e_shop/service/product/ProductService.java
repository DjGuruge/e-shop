package it.gurux.e_shop.service.product;

import it.gurux.e_shop.exception.ProductNotFoundException;
import it.gurux.e_shop.model.Category;
import it.gurux.e_shop.model.Product;
import it.gurux.e_shop.repository.CategoryRepository;
import it.gurux.e_shop.repository.ProductRepository;
import it.gurux.e_shop.request.AddProductRequest;
import it.gurux.e_shop.request.ProductUpdateRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class ProductService implements IProductService {


    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;


    @Override
    public Product addProduct(AddProductRequest request) {
        // chef if the categoru is foind in the db
        // if yes set it as the  new prodict categoru
        // if no the save it as new category
        // the set as the new product category
        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        request.setCategory(category);
        return productRepository.save(createProduct(request, category));

    }

    private Product createProduct(AddProductRequest request, Category category) {
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category

        );

    }

    @Override
    public Product getProductByID(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id).ifPresentOrElse(productRepository::delete,
                () -> {
                    throw new ProductNotFoundException("Product not found");
                });
    }

    @Override
    public Product updateProduct(ProductUpdateRequest request, Long productId) {
        return productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct, request))
                .map(productRepository::save)
                .orElseThrow(() -> new ProductNotFoundException("Producpppt not found"));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }


    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());

        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;
    }


        @Override
        public List<Product> getProductsByCategory (String category){
            return productRepository.findByCategoryName(category);
        }

        @Override
        public List<Product> getProductsByBrand (String brand){
            return productRepository.findByBrandName(brand);
        }

        @Override
        public List<Product> getProductsByCategoryAndBrand (String category, String brand){
            return productRepository.findByCategoryAndBrand(category, brand);
        }


        @Override
        public List<Product> getProductsByBrandAndName (String brand, String name){
            return productRepository.findByBrandAndName(brand, name);
        }

        @Override
        public List<Product> getProductsByName (String name){
            return productRepository.findByName(name);
        }

        @Override
        public Long countProductsByBrandAndName (String brand, String name){
            return productRepository.countByBrandAndName(brand, name);
        }
    }



