package ru.kuz.education.image.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UserImage {

    private MultipartFile file;

    public UserImage(MultipartFile photo) {
        this.file = photo;
    }
}
