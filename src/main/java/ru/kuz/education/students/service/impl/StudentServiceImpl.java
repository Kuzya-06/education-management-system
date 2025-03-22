package ru.kuz.education.students.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kuz.education.auth.config.MyUserDetails;
import ru.kuz.education.auth.model.MyUser;
import ru.kuz.education.mailservice.model.MailType;
import ru.kuz.education.mailservice.service.MailService;
import ru.kuz.education.students.model.Student;
import ru.kuz.education.image.model.UserImage;
import ru.kuz.education.students.repository.StudentRepository;
import ru.kuz.education.image.service.ImageService;
import ru.kuz.education.students.service.StudentService;
import ru.kuz.education.teachers.model.Teacher;
import ru.kuz.education.teachers.repository.TeacherRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

@Service
public class StudentServiceImpl implements StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class); // Логгер

    private final StudentRepository studentRepository;
    private final ImageService imageService;
    private final TeacherRepository teacherRepository;
    private final MailService mailService;

    public StudentServiceImpl(StudentRepository studentRepository,
                              ImageService imageService, TeacherRepository teacherRepository, MailService mailService) {
        this.studentRepository = studentRepository;
        this.imageService = imageService;
        this.teacherRepository = teacherRepository;
        this.mailService = mailService;
    }

    @Override
    @Transactional()
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
            studentRepository.save(student); // Сохраняем в базу данных
        }
        return student;
    }

    @Override
    @Transactional
    public void updateProfile(MyUserDetails userDetails, Student student) {
        // Получаем MyUser из MyUserDetails
        MyUser user = userDetails.getMyUser();
        log.info("User => {}", user);
        // Получаем текущий профиль ученика
        Student existingStudent = studentRepository.findByUserId(user.getId());
        log.info("Student из Repository => {}", existingStudent);

        // Обновляем данные профиля
        if (!student.getFirstName().isBlank()) {
            existingStudent.setFirstName(student.getFirstName());
        }
        if (!student.getLastName().isBlank()) {
            existingStudent.setLastName(student.getLastName());
        }
        if (!student.getEmail().isBlank()) {
            existingStudent.setEmail(student.getEmail());
        }
        if (!student.getPhone().isBlank()) {existingStudent.setPhone(student.getPhone());}

        existingStudent.setBirthDate(student.getBirthDate());
        existingStudent.setCity(student.getCity());



        // Сохраняем изменения в базе данных
        studentRepository.save(existingStudent);
        mailService.sendEmail(existingStudent, MailType.REGISTRATION, new Properties());

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

    @Override
    @Transactional(readOnly = true)
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    @Override
    @Transactional
    public void assignTeacher(Long studentId, Long teacherId) {
        Student student = studentRepository.findById(studentId).orElseThrow();
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow();
        student.getTeachers().clear(); // Очистить предыдущего преподавателя
        student.getTeachers().add(teacher);
        studentRepository.save(student);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getSelectedTeacherId(Long studentId) {
        return studentRepository.findTeacherIdByStudentId(studentId);
    }

    @Override
    @Transactional(readOnly = true)
    public Student getStudentById(Long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Студент не найден"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> getStudentsByTeacherId(Long teacherId) {
        return studentRepository.findByTeachersId(teacherId);
    }

}
