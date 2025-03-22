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

    @GetMapping("/tasks/create")
    public ModelAndView showCreateTaskPage(@AuthenticationPrincipal MyUserDetails userDetails,
                                           Model model) {
        Teacher teacher = teacherService.getProfile(userDetails);
        if (teacher == null) {
            return new ModelAndView("redirect:/login");
        }

        // Получаем всех учеников, которые выбрали этого преподавателя
        List<Student> students = studentService.getStudentsByTeacherId(teacher.getId());

        model.addAttribute("students", students);
        model.addAttribute("task", new Task()); // Пустой объект задачи для формы

        return new ModelAndView("create-task"); // Имя шаблона для страницы создания задачи
    }


    @PostMapping("/tasks")
    public ModelAndView createTask(@ModelAttribute Task task,
                                   @RequestParam Long studentId,
                                   @AuthenticationPrincipal MyUserDetails userDetails) {

        Teacher teacher = teacherService.getProfile(userDetails);
        task.setTeacher(teacher);
        task.setStudent(studentService.getStudentById(studentId));
        taskService.save(task);
        return new ModelAndView("redirect:/teachers/dashboard");
    }



    @PostMapping("/tasks/update/{id}")
    public ModelAndView updateTask(
            @PathVariable Long id,
            @ModelAttribute Task task) {
        log.info("Начало updateTask() по id = {}, task = {}", id, task);
        taskService.updateTask(id, task);
        return new ModelAndView("redirect:/teachers/dashboard");
    }


    @PostMapping("/tasks/delete/{id}")
    public ModelAndView deleteTask(@PathVariable Long id) {
        log.info("Начало deleteTask() по id = {}", id);
        taskService.deleteTask(id);
        log.info("Удалили id = {}", id);
        return new ModelAndView("redirect:/teachers/dashboard");
    }

}
