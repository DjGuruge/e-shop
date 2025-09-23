package it.gurux.e_shop.repository;

import it.gurux.e_shop.model.Image;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ImageRepository extends JpaRepository<Image, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Image i SET i.downloadUrl = CONCAT('/images/image/download/', i.id)")
    void updateAllImageDownloadUrls();
}
