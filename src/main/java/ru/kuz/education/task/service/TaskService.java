package ru.kuz.education.task.service;


import ru.kuz.education.auth.config.MyUserDetails;
import ru.kuz.education.task.model.Task;
import ru.kuz.education.task.model.TaskImage;

import java.time.Duration;
import java.util.List;

public interface TaskService {

    /**
     * Получить задачу по ID.
     *
     * @param id ID задачи
     * @return задача
     */
    Task getTaskById(Long id);

    List<Task> getAllByTeacherId(Long teacherId);

    List<Task> findTasksByStudentAndTeacher(Long studentId, Long teacherId);

    Task save(Task task);

    List<Task> getAllTasksForCurrentStudent(Long studentId);

    /**
     * Получить все задачи, которые должны быть выполнены в ближайшее время.
     *
     * @param duration временной интервал
     * @return список задач
     */
    List<Task> getAllSoonTasks(Duration duration);

    /**
     * Обновить задачу.
     */
    void updateTask(Long id, Task task);

    /**
     * Создать новую задачу.
     *
     * @param task   задача
     * @param userDetails userDetails
     * @return созданная задача
     */
    Task createTask(Task task, MyUserDetails userDetails);

    /**
     * Удалить задачу по ID.
     *
     * @param id ID задачи
     */
    void deleteTask(Long id);


    /**
     * Загрузить изображение для задачи.
     *
     * @param id    ID задачи
     * @param image изображение
     */
    void uploadTaskImage(Long id, TaskImage image);

    /**
     * Получить все задачи.
     *
     * @return список всех задач
     */
    List<Task> getAllTasks();

    List<Task> findTasksByStudentId(Long studentId);
}