package ru.kuz.education.task.service.impl;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kuz.education.auth.config.MyUserDetails;
import ru.kuz.education.auth.model.MyUser;
import ru.kuz.education.students.service.impl.StudentServiceImpl;
import ru.kuz.education.task.model.Task;
import ru.kuz.education.task.model.TaskImage;
import ru.kuz.education.task.repository.TaskRepository;
import ru.kuz.education.task.service.TaskService;
import ru.kuz.education.teachers.model.Teacher;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class); // Логгер

    private final TaskRepository taskRepository;

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Задача не найдена"));
    }

    @Override
    public List<Task> getAllByTeacherId(Long teacherId) {
        return taskRepository.findByTeacherId(teacherId);
    }

    @Override
    public List<Task> findTasksByStudentAndTeacher(Long studentId, Long teacherId) {
        return taskRepository.findByStudentIdAndTeacherId(studentId, teacherId);
    }

    @Override
    public List<Task> getAllSoonTasks(Duration duration) {
        LocalDateTime now = LocalDateTime.now();
        return taskRepository.findAllSoonTasks(now, now.plus(duration));
    }

    @Override
    @Transactional
    public Task save(Task task) {
        return taskRepository.save(task);
    }


    @Override
    @Transactional
    public Task updateTask(Task task) {
        Task existingTask = getTaskById(task.getId());
        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setStatus(task.getStatus());
        existingTask.setExpirationDate(task.getExpirationDate());
        existingTask.setVideoUrl(task.getVideoUrl());
        return taskRepository.save(existingTask);
    }

    @Override
    @Transactional
    public Task createTask(Task task, MyUserDetails userDetails) {
        // Получаем MyUser из MyUserDetails
        MyUser myUser = userDetails.getMyUser();
        log.info("User => {}", myUser);
        Teacher teacher = myUser.getTeacher();

        log.info("myUser.teacher => {}", teacher);
        task.setTeacher(teacher); // Устанавливаем ID teacher
        return taskRepository.save(task);
    }

    @Override
    @Transactional
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }



    @Override
    @Transactional
    public void uploadTaskImage(Long id, TaskImage image) {
        Task task = getTaskById(id);
        task.getImages().add(image.getFile().getOriginalFilename());
        taskRepository.save(task);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> findTasksByStudentId(Long studentId) {
        log.info("начало метода findTasksByStudentId() с studentId = {}", studentId);
        return taskRepository.findTasksByStudentId(studentId);
    }
}