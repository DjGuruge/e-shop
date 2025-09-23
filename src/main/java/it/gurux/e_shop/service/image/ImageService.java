package it.gurux.e_shop.service.image;

import it.gurux.e_shop.dto.ImageDto;
import it.gurux.e_shop.exception.ResourceNotFoundException;
import it.gurux.e_shop.model.Image;
import it.gurux.e_shop.model.Product;
import it.gurux.e_shop.repository.ImageRepository;
import it.gurux.e_shop.service.product.IProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ImageService implements IImageService{

    private final ImageRepository imageRepository;
    private final IProductService iProductService;

    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Image not found"));
    }

    @Override
    public void deleteImageById(Long id) {
        imageRepository.findById(id)
                .ifPresentOrElse(imageRepository ::delete,
                        ()-> {
                            throw new ResourceNotFoundException(" Image not fund" + id);
                        });
    }

    @Override
    public List<ImageDto> saveImages(List<MultipartFile> files, Long productId) {
        Product product = iProductService.getProductByID(productId);
        List<ImageDto> savedImageDto = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                // 1. Save the image to the repository to generate an ID
                Image savedImage = imageRepository.save(image);

                // 2. Now that we have the ID, construct the correct download URL
                String downloadUrl = "/images/image/download/" + savedImage.getId();
                savedImage.setDownloadUrl(downloadUrl);

                // 3. Save the image again to persist the downloadUrl.
                //    Crucially, use the returned object from this save.
                Image fullyUpdatedImage = imageRepository.save(savedImage);

                // 4. Build the DTO using the fully updated object
                ImageDto imageDto = new ImageDto();
                imageDto.setId(fullyUpdatedImage.getId());
                imageDto.setFileName(fullyUpdatedImage.getFileName());
                imageDto.setDownloadUrl(fullyUpdatedImage.getDownloadUrl());
                savedImageDto.add(imageDto);

            } catch (IOException | SQLException e) {
                throw new RuntimeException((e.getMessage()));
            }
        }
        return savedImageDto;
    }

    @Override
    public void updateImage(Long imageId, MultipartFile file) {
        Image image = getImageById(imageId);
        try {
            image.setFileName(file.getOriginalFilename());
            image.setFileName(file.getOriginalFilename());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }



    @Override
    public void updateAllImageDownloadUrls() {
        imageRepository.updateAllImageDownloadUrls();
    }
}
