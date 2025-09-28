package it.gurux.e_shop.service.image;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import it.gurux.e_shop.dto.ImageDto;
import it.gurux.e_shop.model.Image;

public interface IImageService {
    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDto> saveImages(List<MultipartFile> file, Long productId);
    void updateImage(Long id,MultipartFile file);
    void updateAllImageDownloadUrls();
}
