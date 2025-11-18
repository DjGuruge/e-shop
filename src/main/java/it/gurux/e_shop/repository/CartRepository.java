package it.gurux.e_shop.repository;

import it.gurux.e_shop.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUserId(Long userId);
    @Query("SELECT MAX(c.id) FROM Cart c")
    Optional<Long> findMaxId();
}
