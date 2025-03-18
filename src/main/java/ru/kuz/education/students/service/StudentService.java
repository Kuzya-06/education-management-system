package ru.kuz.education.students.service;

import ru.kuz.education.auth.config.MyUserDetails;
import ru.kuz.education.students.model.Student;
import ru.kuz.education.image.model.UserImage;
import ru.kuz.education.teachers.model.Teacher;

import java.io.IOException;
import java.util.List;

public interface StudentService {

    Student getProfile(MyUserDetails userDetails);

    void updateProfile(MyUserDetails userDetails, Student student);

    void uploadImage(Long id, UserImage image) throws IOException;

    List<Teacher> getAllTeachers();

    void assignTeacher(Long studentId, Long teacherId);

    Long getSelectedTeacherId(Long studentId);

    Student getStudentById(Long studentId);

    List<Student> getStudentsByTeacherId(Long teacherId);

}
