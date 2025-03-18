package ru.kuz.education.students.service;

import ru.kuz.education.auth.config.MyUserDetails;
import ru.kuz.education.students.model.Student;
import ru.kuz.education.image.model.UserImage;

import java.io.IOException;

public interface StudentService {

    Student getProfile(MyUserDetails userDetails);

    void updateProfile(MyUserDetails userDetails, Student student);

    void uploadImage(Long id, UserImage image) throws IOException;

}
