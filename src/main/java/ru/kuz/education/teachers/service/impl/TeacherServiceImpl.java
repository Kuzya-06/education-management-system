package ru.kuz.education.teachers.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kuz.education.auth.config.MyUserDetails;
import ru.kuz.education.auth.model.MyUser;
import ru.kuz.education.image.model.UserImage;
import ru.kuz.education.image.service.ImageService;
import ru.kuz.education.students.service.impl.StudentServiceImpl;
import ru.kuz.education.task.model.Task;
import ru.kuz.education.task.repository.TaskRepository;
import ru.kuz.education.teachers.model.Teacher;
import ru.kuz.education.teachers.repository.TeacherRepository;
import ru.kuz.education.teachers.service.TeacherService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TeacherServiceImpl implements TeacherService {

    private static final Logger log = LoggerFactory.getLogger(TeacherServiceImpl.class); // Логгер

    private final TeacherRepository teacherRepository;
    private final ImageService imageService;
    private final TaskRepository taskRepository;

    public TeacherServiceImpl(TeacherRepository teacherRepository, ImageService imageService, TaskRepository taskRepository) {
        this.teacherRepository = teacherRepository;
        this.imageService = imageService;
        this.taskRepository = taskRepository;
    }


    @Override
    @Transactional  // Добавляем транзакцию
    public Teacher getProfile(MyUserDetails userDetails) {
        // Получаем MyUser из MyUserDetails
        MyUser user = userDetails.getMyUser();
        log.info("Получен User {}", user);
        Optional<Teacher> teacher = teacherRepository.findByUserId(user.getId());
        Teacher newTeacher;
        if (teacher.isPresent()) {
            return teacher.get();
        } else {
            // Если профиль не найден, создаем новый
            newTeacher = new Teacher();
            newTeacher.setUser(user);
            newTeacher.setCity("Город");
            newTeacher.setLastName("Фамилия");
            newTeacher.setFirstName("Имя");
            newTeacher.setSpecialty("Специальность");
            newTeacher.setDescription("Описание");
            newTeacher.setPhone("+71234567890");
            newTeacher.setEmail("email@email.com");
            newTeacher.setBirthDate(LocalDate.now());

            teacherRepository.save(newTeacher); // Сохраняем в базу данных
        }

        return newTeacher;
    }

    @Override
    @Transactional
    public void updateProfile(MyUserDetails userDetails, Teacher teacher) {
        // Получаем MyUser из MyUserDetails
        MyUser user = userDetails.getMyUser();
        log.info("User => {}", user);
        // Получаем текущий профиль преподавателя
        Optional<Teacher> existingTeacher = teacherRepository.findByUserId(user.getId());
        log.info("Teacher из Repository => {}", existingTeacher);

        if (existingTeacher.isPresent()) {
            // Обновляем данные профиля
            Teacher teacherToUpdate = existingTeacher.get();
            teacherToUpdate.setFirstName(teacher.getFirstName());
            teacherToUpdate.setLastName(teacher.getLastName());
            teacherToUpdate.setSpecialty(teacher.getSpecialty());
            teacherToUpdate.setDescription(teacher.getDescription());
            teacherToUpdate.setBirthDate(teacher.getBirthDate());
            teacherToUpdate.setEmail(teacher.getEmail());
            teacherToUpdate.setPhone(teacher.getPhone());
            teacherToUpdate.setCity(teacher.getCity());
            log.info("teacherToUpdate => {}", teacherToUpdate);
            // Устанавливаем пользователя
            teacherToUpdate.setUser(user);
            log.info("teacherToUpdate.setUser(user) => {}", teacherToUpdate.getUser());

            // Сохраняем изменения в базе данных
            teacherRepository.save(teacherToUpdate);
            log.info("Профиль преподавателя успешно обновлен.");
        } else {
            log.info("Профиль преподавателя null");
        }

    }


    @Override
    @Transactional
    public void uploadImage(Long id, UserImage image) throws IOException {
        log.info("Начало uploadImage(), где id = {}, image.getFile = {}", id, image.getFile());
        String fileName = imageService.upload(image);
        log.info("fileName = {}", fileName);
        teacherRepository.addImage(id, fileName);
    }
}
