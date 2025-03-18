package ru.kuz.education.teachers.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import ru.kuz.education.auth.config.MyUserDetails;
import ru.kuz.education.image.model.UserImage;
import ru.kuz.education.students.model.Student;
import ru.kuz.education.students.service.StudentService;
import ru.kuz.education.students.service.props.MinioProperties;
import ru.kuz.education.task.model.Task;
import ru.kuz.education.task.service.TaskService;
import ru.kuz.education.teachers.model.Teacher;
import ru.kuz.education.teachers.service.TeacherService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

    private static final Logger log = LoggerFactory.getLogger(TeacherController.class); // Логгер

    private final TaskService taskService;
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final MinioProperties minioProperties;

    public TeacherController(TaskService taskService, StudentService studentService, TeacherService teacherService, MinioProperties minioProperties) {
        this.taskService = taskService;
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.minioProperties = minioProperties;
    }


    @GetMapping("/dashboard")
    public ModelAndView getDashboard(@AuthenticationPrincipal MyUserDetails userDetails,
                                     Model model) {

        log.info("Начало getDashboard()");
        log.info("userDetails.getMyUser() = {}", userDetails.getMyUser());

        Teacher teacher = teacherService.getProfile(userDetails);
        if (teacher == null) {
            return new ModelAndView("redirect:/login");
        }

        Long teacherId = teacher.getId();

        // Получаем всех учеников, которые выбрали этого преподавателя
        List<Student> students = studentService.getStudentsByTeacherId(teacherId);

        List<Task> tasks = taskService.getAllByTeacherId(teacherId);

        model.addAttribute("teacher", teacher);
        model.addAttribute("students", students);
        model.addAttribute("tasks", tasks);
        model.addAttribute("minio", minioProperties); // Передаем объект student в модель
        log.info("MinioProperties: {}", minioProperties); // Логирование minio properties

        return new ModelAndView("teacher-dashboard");
    }


    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
    public ModelAndView getProfile(
            @AuthenticationPrincipal MyUserDetails userDetails,
            Model model) {

        log.info("Начало getProfile()");
        if (userDetails == null) {
            log.warn("Пользователь не аутентифицирован. Перенаправление на страницу входа.");
            return new ModelAndView("redirect:/login");
        }

        Teacher teacher = teacherService.getProfile(userDetails);  // Замените на реальный ID преподавателя
        if (teacher == null || teacher.getUser() == null) {
            log.warn("Профиль преподавателя не найден.");
            return new ModelAndView("redirect:/error"); // Перенаправление на страницу ошибки
        }

        model.addAttribute("teacher", teacher);
        model.addAttribute("minio", minioProperties); // Передаем объект student в модель
        log.info("Minio properties: {}", minioProperties); // Логирование minio properties

        return new ModelAndView("teacher-profile");
    }

    @PostMapping("/profile")
    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
    public ModelAndView updateProfile(
            @AuthenticationPrincipal MyUserDetails userDetails,
            @ModelAttribute Teacher teacher) {

        log.info("Начало updateProfile");

        teacherService.updateProfile(userDetails, teacher);
        return new ModelAndView("redirect:/teachers/dashboard");
    }

    @PostMapping(value = "/profile/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
    public void uploadImage(
            @PathVariable(value = "id") final Long userId,
            @Validated @ModelAttribute final UserImage image) throws IOException {
        log.info("Начало uploadImage(), где userId =  {}, image = {}", userId, image);
        teacherService.uploadImage(userId, image);
    }

    @PostMapping("/tasks")
    public ModelAndView createTask(@ModelAttribute Task task,
                                   @RequestParam Long studentId,
                                   @AuthenticationPrincipal MyUserDetails userDetails) {
//        log.info("Начало createTask()");
//        if (userDetails == null) {
//            log.warn("Пользователь не аутентифицирован. Перенаправление на страницу входа.");
//            return new ModelAndView("redirect:/login");
//        }
//        log.info("Task => {}", task);
//        Task createTask = taskService.createTask(task, userDetails);// Замените на реальный ID преподавателя
//        log.info("createTask => {}", createTask);
//        return new ModelAndView("teacher-dashboard");

        Teacher teacher = teacherService.getProfile(userDetails);
        task.setTeacher(teacher);
        task.setStudent(studentService.getStudentById(studentId));
        taskService.save(task);
        return new ModelAndView("redirect:/teachers/dashboard");
    }

//    @GetMapping("/teachers/dashboard")
//    public String getTeacherDashboard(Model model, @AuthenticationPrincipal MyUserDetails userDetails) {
//        Long teacherId = userDetails.getMyUser().getTeacher().getId();
//        List<Student> students = studentService.findStudentsByTeacherId(teacherId);
//        model.addAttribute("students", students);
//        return "teacher-dashboard";
//    }
//
//    @PostMapping("/teachers/assign-task")
//    public String assignTask(@RequestParam Long studentId, @ModelAttribute Task task) {
//        taskService.create(task, studentId);
//        return "redirect:/teachers/dashboard";
//    }

}
