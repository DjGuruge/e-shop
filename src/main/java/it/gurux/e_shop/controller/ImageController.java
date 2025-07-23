package it.gurux.e_shop.controller;

import io.micrometer.common.util.internal.logging.InternalLogger;
import it.gurux.e_shop.dto.ImageDto;
import it.gurux.e_shop.model.Image;
import it.gurux.e_shop.model.Product;
import it.gurux.e_shop.response.ApiResponse;
import it.gurux.e_shop.service.image.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;


@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/images")
public class ImageController {
    private final IImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> saveImages(@RequestParam List<MultipartFile> files,@RequestParam Long productId) {
        try {
            List<ImageDto> imageDtos = imageService.saveImages(files, productId);
            return ResponseEntity.ok(new ApiResponse("Upload success !", imageDtos));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Upload failed!", e.getMessage()));
        }

    }


    public ResponseEntity<ApiResponse> downloadImage(@PathVariable Long imageId){
        Image image = imageService.getImageById(imageId);


    }






}
