package ru.kuz.education.mailservice.service;

import ru.kuz.education.mailservice.model.MailType;
import ru.kuz.education.students.model.Student;

import java.util.Properties;

public interface MailService {

    void sendEmail(
            Student student,
            MailType type,
            Properties params
    );

}
