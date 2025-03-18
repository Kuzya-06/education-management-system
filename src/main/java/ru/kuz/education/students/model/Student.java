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
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
//            nullable = false,
            name = "first_name"
    )
    private String firstName;

    @Column(
//            nullable = false,
            name = "last_name")
    private String lastName;

    @Column(
//            nullable = false,
            name = "birth_date")
    private LocalDate birthDate;

//    @Column(nullable = false)
    private String city;

//    @Column(nullable = false)
    private String phone;

    @Column(
//            nullable = false,
            unique = true)
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