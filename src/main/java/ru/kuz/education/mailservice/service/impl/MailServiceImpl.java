package ru.kuz.education.mailservice.service.impl;

import freemarker.template.Configuration;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.kuz.education.mailservice.model.MailType;
import ru.kuz.education.mailservice.service.MailService;
import ru.kuz.education.students.controller.StudentController;
import ru.kuz.education.students.model.Student;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private static final Logger log = LoggerFactory.getLogger(MailServiceImpl.class); // Логгер

    private final Configuration configuration;
    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(
            final Student user,
            final MailType type,
            final Properties params
    ) {
        switch (type) {
            case REGISTRATION -> sendRegistrationEmail(user, params);
            case REMINDER -> sendReminderEmail(user, params);
            default -> {
            }
        }
    }

    @SneakyThrows
    private void sendRegistrationEmail(final Student user,
                                       final Properties params) {
        log.info("Sending registration email to " + user.getEmail());
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,
                false,
                "UTF-8");
        helper.setSubject("Спасибо за регистрацию, " + user.getFirstName());
        helper.setTo(user.getEmail());
        String emailContent = getRegistrationEmailContent(user, params);
        helper.setText(emailContent, true);
        mailSender.send(mimeMessage);
    }

    @SneakyThrows
    private void sendReminderEmail(final Student user,
                                   final Properties params) {
        log.info("Sending reminder email to " + user.getEmail());
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,
                false,
                "UTF-8");
        helper.setSubject("У вас есть задача которая закончится через 1 час");
        helper.setTo(user.getEmail());
        String emailContent = getReminderEmailContent(user, params);
        helper.setText(emailContent, true);
        mailSender.send(mimeMessage);
    }

    @SneakyThrows
    private String getRegistrationEmailContent(final Student user,
                                               final Properties properties) {
        log.info("Sending registration email Content to " + user.getEmail());
        StringWriter writer = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("name", user.getFirstName());
        configuration.getTemplate("register.ftlh")
                .process(model, writer);
        return writer.getBuffer().toString();
    }

    @SneakyThrows
    private String getReminderEmailContent(final Student user,
                                           final Properties properties) {
        log.info("Sending reminder email Content to " + user.getEmail());
        StringWriter writer = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("name", user.getFirstName());
        model.put("title", properties.getProperty("task.title"));
        model.put("description", properties.getProperty("task.description"));
        configuration.getTemplate("reminder.ftlh")
                .process(model, writer);
        return writer.getBuffer().toString();
    }

}
