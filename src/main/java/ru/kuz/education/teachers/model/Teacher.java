package ru.kuz.education.teachers.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.kuz.education.auth.model.MyUser;
import ru.kuz.education.students.model.Student;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString(exclude = "user")
@Entity
@Table(name = "teachers")
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
//            nullable = false,
            name = "first_name"
    )
    private String firstName;

    @Column(name = "last_name")
    private String lastName;



    @Column(name = "birth_date")
    private LocalDate birthDate;


    private String city;


    private String phone;

    @Column(unique = true)
    private String email;

    @Column
    private String photoUrl; // Ссылка на фотографию

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
//    @JsonManagedReference // Указываем, что это владеющая сторона
    @JsonIgnore
    private MyUser user; // Связь с пользователем

    @ManyToMany(mappedBy = "teachers")
    private Set<Student> students = new HashSet<>();

    // Убираем Lombok-аннотированные hashCode() и equals()
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}