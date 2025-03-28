package ru.kuz.education.students.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.ToString;
import ru.kuz.education.auth.model.MyUser;
import ru.kuz.education.teachers.model.Teacher;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@ToString(exclude = "user")
@Entity
@Table(name = "students", uniqueConstraints = @UniqueConstraint(columnNames = {"email"}, name = "unique_email_not_null"))
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    private String city;

    private String phone;

    @Column
    private String email;

    @Column
    private String photoUrl; // Ссылка на фотографию

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
//    @JsonManagedReference // Указываем, что это владеющая сторона
    @JsonIgnore
    private MyUser user; // Связь с пользователем

    @ManyToMany
    @JoinTable(
            name = "student_teacher",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id")
    )
    @JsonIgnore
    private Set<Teacher> teachers = new HashSet<>();
}