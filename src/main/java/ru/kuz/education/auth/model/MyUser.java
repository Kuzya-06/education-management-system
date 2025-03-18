package ru.kuz.education.auth.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.kuz.education.students.model.Student;
import ru.kuz.education.teachers.model.Teacher;

@Getter
@Setter
@ToString(exclude = {"student", "teacher"})
@Entity
@Table(name="my_users")
public class MyUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    private String name;

    private String password;

    private String roles;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference // Указываем, что это зависимая сторона
    private Student student; // Связь с профилем ученика

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference // Указываем, что это зависимая сторона
    private Teacher teacher; // Связь с профилем преподавателя

    // Убираем Lombok-аннотированные hashCode() и equals()
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
