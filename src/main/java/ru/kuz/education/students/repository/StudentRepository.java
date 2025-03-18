package ru.kuz.education.students.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kuz.education.students.model.Student;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Student findByUserId(Long userId); // Поиск профиля ученика по ID пользователя

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE students
            SET photo_url = CONCAT('', :photo_url, '')
            WHERE user_id = :user_id
            """, nativeQuery = true)
    void addImage(
            @Param("user_id") Long id,
            @Param("photo_url") String fileName
    );

    @Query("SELECT t.id FROM Student s JOIN s.teachers t WHERE s.id = :studentId")
    Long findTeacherIdByStudentId(@Param("studentId") Long studentId);

    List<Student> findByTeachersId(Long teacherId);

}
