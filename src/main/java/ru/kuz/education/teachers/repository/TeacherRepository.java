package ru.kuz.education.teachers.repository;


import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kuz.education.teachers.model.Teacher;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    /**
     * Найти преподавателя по ID пользователя.
     *
     * @param userId ID пользователя
     * @return преподаватель
     */
    @EntityGraph(attributePaths = {"students"})
    Optional<Teacher> findByUserId(Long userId);

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE teachers
            SET photo_url = CONCAT('', :photo_url, '')
            WHERE user_id = :user_id
            """, nativeQuery = true)
    void addImage(
            @Param("user_id") Long id,
            @Param("photo_url") String fileName
    );
}