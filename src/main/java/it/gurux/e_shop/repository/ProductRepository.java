package it.gurux.e_shop.repository;

import it.gurux.e_shop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product , Long > {
    List<Product> findByCategoryName(String category);

    List<Product> findByBrand(String brand);

    List<Product> findByCategory_NameAndBrand(String category, String brand);

    List<Product> findByNameAndBrand(String brand,String name);

    Long countByBrandAndName(String brand, String name);

    void deleteById(Long id);

    List<Product> findByName(String name);

}
