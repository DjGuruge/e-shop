package it.gurux.e_shop.service.image;

import it.gurux.e_shop.dto.ImageDto;
import it.gurux.e_shop.model.Image;
import it.gurux.e_shop.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDto> saveImages(List<MultipartFile> file, Long productId);
    void updateImage(Long id,MultipartFile file);
    void updateAllImageDownloadUrls();
}
