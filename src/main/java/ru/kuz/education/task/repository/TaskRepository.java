package ru.kuz.education.task.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.kuz.education.task.model.Task;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Найти все задачи по ID пользователя.
     *
     * @param userId ID пользователя
     * @return список задач
     */
    List<Task> findAllByUserId(Long userId);

    /**
     * Найти все задачи, которые должны быть выполнены в указанный период.
     *
     * @param start начало периода
     * @param end   конец периода
     * @return список задач
     */
    @Query("SELECT t FROM Task t WHERE t.expirationDate BETWEEN :start AND :end")
    List<Task> findAllSoonTasks(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT t FROM Task t JOIN t.userId te JOIN t.student s WHERE s.id = :studentId")
    List<Task> findTasksByStudentId(@Param("studentId") Long studentId);

}