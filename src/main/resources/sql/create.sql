CREATE TABLE student_teacher (
                                 student_id BIGINT NOT NULL,
                                 teacher_id BIGINT NOT NULL,
                                 PRIMARY KEY (student_id, teacher_id),
                                 FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
                                 FOREIGN KEY (teacher_id) REFERENCES teachers(id) ON DELETE CASCADE
);