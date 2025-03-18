package ru.kuz.education.teachers.service;

import ru.kuz.education.auth.config.MyUserDetails;
import ru.kuz.education.image.model.UserImage;
import ru.kuz.education.task.model.Task;
import ru.kuz.education.teachers.model.Teacher;

import java.io.IOException;
import java.util.List;

public interface TeacherService {


    Teacher getProfile(MyUserDetails userDetails);

    void updateProfile(MyUserDetails userDetails, Teacher teacher);

    void uploadImage(Long id, UserImage image) throws IOException;


}
