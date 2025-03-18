package ru.kuz.education.students.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kuz.education.auth.config.MyUserDetails;
import ru.kuz.education.auth.model.MyUser;
import ru.kuz.education.students.model.Student;
import ru.kuz.education.image.model.UserImage;
import ru.kuz.education.students.repository.StudentRepository;
import ru.kuz.education.image.service.ImageService;
import ru.kuz.education.students.service.StudentService;

import java.io.IOException;
import java.time.LocalDate;

@Service
public class StudentServiceImpl implements StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class); // Логгер

    private final StudentRepository studentRepository;

    private final ImageService imageService;

    public StudentServiceImpl(StudentRepository studentRepository, ImageService imageService) {
        this.studentRepository = studentRepository;

        this.imageService = imageService;
    }

    @Override
    public Student getProfile(MyUserDetails userDetails) {
        // Получаем MyUser из MyUserDetails
        MyUser user = userDetails.getMyUser();
        log.info("Получен User {}", user);

        // Получаем профиль ученика по ID пользователя
        Student student = studentRepository.findByUserId(user.getId());

        // Если профиль не найден, создаем новый
        if (student == null) {
            student = new Student();
            student.setUser(user); // Устанавливаем связь с пользователем
            student.setCity("Город");
            student.setLastName("Фамилия");
            student.setFirstName("Имя");
            student.setPhone("+71234567890");
            student.setEmail("email@email.com");
            student.setBirthDate(LocalDate.now());

            studentRepository.save(student); // Сохраняем в базу данных
        }
        return student;
    }

    @Override
    public void updateProfile(MyUserDetails userDetails, Student student) {
        // Получаем MyUser из MyUserDetails
        MyUser user = userDetails.getMyUser();
        log.info("User => {}", user);
        // Получаем текущий профиль ученика
        Student existingStudent = studentRepository.findByUserId(user.getId());
        log.info("Student из Repository => {}", existingStudent);

        // Обновляем данные профиля
        existingStudent.setFirstName(student.getFirstName());
        existingStudent.setLastName(student.getLastName());
        existingStudent.setBirthDate(student.getBirthDate());
        existingStudent.setCity(student.getCity());
        existingStudent.setPhone(student.getPhone());
        existingStudent.setEmail(student.getEmail());

        // Сохраняем изменения в базе данных
        studentRepository.save(existingStudent);
        log.info("Профиль ученика успешно обновлен.");

    }

    @Override
    @Transactional
    public void uploadImage(Long id, UserImage image) throws IOException {
        log.info("Начало uploadImage(), где id = {}, image.getFile = {}", id, image.getFile());
        String fileName = imageService.upload(image);
        log.info("fileName = {}", fileName);
        studentRepository.addImage(id, fileName);
    }

}
