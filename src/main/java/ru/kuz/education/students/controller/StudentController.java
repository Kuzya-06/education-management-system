package ru.kuz.education.students.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import ru.kuz.education.auth.config.MyUserDetails;
import ru.kuz.education.students.model.Student;
import ru.kuz.education.image.model.UserImage;
import ru.kuz.education.students.service.StudentService;
import ru.kuz.education.students.service.props.MinioProperties;
import ru.kuz.education.task.model.Task;
import ru.kuz.education.task.service.TaskService;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/students")
public class StudentController {

    private static final Logger log = LoggerFactory.getLogger(StudentController.class); // Логгер


    private final StudentService studentService;
    private final TaskService taskService;
    private final MinioProperties minioProperties;

    public StudentController(StudentService studentService, TaskService taskService, MinioProperties minioProperties) {
        this.studentService = studentService;
        this.taskService = taskService;
        this.minioProperties = minioProperties;
    }


    @GetMapping("/dashboard")
    public ModelAndView getDashboard(
            @AuthenticationPrincipal MyUserDetails userDetails,
            Model model) {
        log.info("Начало getDashboard()");
        log.info("{}", userDetails.getMyUser());
        // Пример задачи от преподавателя
        String task = "Решить задачу по математике до 25.03.2025.";

        Student student = studentService.getProfile(userDetails);

        model.addAttribute("student", student); // Передаем объект student в модель
        model.addAttribute("minio", minioProperties); // Передаем объект student в модель
        log.info("Minio properties: {}", minioProperties); // Логирование minio properties

        Long studentId = userDetails.getMyUser().getStudent().getId();
        List<Task> tasks = taskService.findTasksByStudentId(studentId);
        model.addAttribute("task", task);
        return new ModelAndView("student-dashboard");
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ModelAndView getProfile(
            @AuthenticationPrincipal MyUserDetails userDetails,
            Model model) {

        log.info("Начало getProfile()");
        if (userDetails == null) {
            log.warn("Пользователь не аутентифицирован. Перенаправление на страницу входа.");
            return new ModelAndView("redirect:/login");
        }

        Student student = studentService.getProfile(userDetails);
        if (student == null || student.getUser() == null) {
            log.warn("Профиль ученика или пользователь не найден.");
            return new ModelAndView("redirect:/error"); // Перенаправление на страницу ошибки
        }

        model.addAttribute("student", student); // Передаем объект student в модель
        model.addAttribute("minio", minioProperties); // Передаем объект student в модель
        log.info("Minio properties: {}", minioProperties); // Логирование minio properties

        return new ModelAndView("student-profile");
    }

    @PostMapping("/profile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ModelAndView updateProfile(
            @AuthenticationPrincipal MyUserDetails userDetails,
            @ModelAttribute Student student) {

        log.info("Начало updateProfile");

        studentService.updateProfile(userDetails, student);
        return new ModelAndView("redirect:/students/dashboard");
    }


    @PostMapping(value = "/profile/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public void uploadImage(
            @PathVariable(value = "id") final Long userId,
            @Validated @ModelAttribute final UserImage image) throws IOException {
        log.info("Начало uploadImage(), где userId =  {}, image = {}", userId, image);
        studentService.uploadImage(userId, image);
    }
}
