package ru.kuz.education.mailservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.kuz.education.mailservice.model.MailType;
import ru.kuz.education.mailservice.service.MailService;
import ru.kuz.education.mailservice.service.Reminder;
import ru.kuz.education.students.model.Student;
import ru.kuz.education.students.service.StudentService;
import ru.kuz.education.task.model.Task;
import ru.kuz.education.task.service.TaskService;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class ReminderImpl implements Reminder {

    private static final Logger log = LoggerFactory.getLogger(ReminderImpl.class); // Логгер

    private final TaskService taskService;
    private final StudentService userService;
    private final MailService mailService;
    /**
     * Период времени (1 час), который используется для поиска задач, срок выполнения которых истекает в ближайший час.
     */
    private final Duration duration = Duration.ofHours(1);

    /**
     * Отвечает за отправку напоминаний ученикам о задачах, срок выполнения которых скоро истекает.
     */

    //    @Scheduled(cron = "0 0 * * * *") // раз в час
    @Scheduled(cron = "0 * * * * *") // раз в минуту
    @Override
    public void remindForTask() {
        log.info("Starting reminder");
        List<Task> tasks = taskService.getAllSoonTasks(duration);
        tasks.forEach(task -> {
            Student student = userService.getStudentById(tasks.get(0).getStudent().getId());
            Properties properties = new Properties();
            properties.setProperty("task.title", task.getTitle());
            properties.setProperty("task.description", task.getDescription());
            mailService.sendEmail(student, MailType.REMINDER, properties);
        });
    }

}
