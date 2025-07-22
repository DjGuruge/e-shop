package it.gurux.e_shop.repository;

import it.gurux.e_shop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product , Long > {
    List<Product> findByCategoryName(String category);

    List<Product> findByBrandName(String brand);

    List<Product> findByCategoryAndBrand(String category, String brand);

    List<Product> findByProductName(String name);

    List<Product> findNameAndBrand(String name,String brand);

    Long countByBrandAndName(String brand, String name);

    Long countProductsByBrand(String brand);

    void deleteById(Long id);

    List<Product> findByName(String name);

    List<Product> findByBrandAndName(String brand, String name);
}
