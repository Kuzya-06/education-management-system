package ru.kuz.education.students.config;


import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import ru.kuz.education.students.service.props.MinioProperties;

@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class MinioConfig {


    private final MinioProperties minioProperties;


    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minioProperties.getUrl())
                .credentials(minioProperties.getAccessKey(),
                        minioProperties.getSecretKey())
                .build();
    }


}
