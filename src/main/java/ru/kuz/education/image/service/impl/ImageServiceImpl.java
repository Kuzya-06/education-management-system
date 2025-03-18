package ru.kuz.education.image.service.impl;


import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.kuz.education.students.exception.ImageUploadException;
import ru.kuz.education.image.model.UserImage;
import ru.kuz.education.image.service.ImageService;
import ru.kuz.education.students.service.props.MinioProperties;

import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private static final Logger log = LoggerFactory.getLogger(ImageServiceImpl.class); // Логгер


    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    @Override
    public String upload(final UserImage image) {
        log.info("Начало upload(), где image.getFile = {}",  image.getFile());

        try {
            createBucket();
        } catch (Exception e) {
            throw new ImageUploadException("Загрузка изображения не удалась: "
                    + e.getMessage());
        }
        MultipartFile file = image.getFile();
        log.info("MultipartFile file = {}", file.getOriginalFilename());

        if (file.isEmpty() || file.getOriginalFilename() == null) {
            throw new ImageUploadException("Изображение должно иметь имя");
        }
        String fileName = generateFileName(file);
        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
        } catch (Exception e) {
            throw new ImageUploadException("Загрузка изображения не удалась: "
                    + e.getMessage());
        }
        saveImage(inputStream, fileName);
        return fileName;
    }

    @SneakyThrows
    private void createBucket() {
        log.info("Начало createBucket()");
        log.info("minioProperties.getBucket() = {}", minioProperties.getBucket());

        boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(minioProperties.getBucket())
                .build());
        log.info("found = {}",found);

        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .build());
        }
    }

    private String generateFileName(final MultipartFile file) {
        String extension = getExtension(file);
        return UUID.randomUUID() + "." + extension;
    }

    private String getExtension(final MultipartFile file) {
        return file.getOriginalFilename()
                .substring(file.getOriginalFilename()
                        .lastIndexOf(".") + 1);
    }

    @SneakyThrows
    private void saveImage(final InputStream inputStream, final String fileName) {
        minioClient.putObject(PutObjectArgs.builder()
                .stream(inputStream, inputStream.available(), -1)
                .bucket(minioProperties.getBucket())
                .object(fileName)
                .build());
    }

}
