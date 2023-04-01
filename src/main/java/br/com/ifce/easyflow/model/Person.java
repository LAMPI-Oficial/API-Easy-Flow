package br.com.ifce.easyflow.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "person")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    private Long id;

    @Column(name = "person_name")
    private String name;

    @Column(name = "person_email")
    private String email;

    @Column(name = "person_admin")
    private boolean person_admin = false;

    @JsonIgnore
    @OneToOne(targetEntity = User.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @OneToOne(targetEntity = Course.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "course_id")
    private Course course;

    @JsonIgnore
    @OneToOne(targetEntity = StudyArea.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "study_area_id")
    private StudyArea study_area;

    @JsonIgnore
    @OneToMany(mappedBy = "person",targetEntity = Daily.class, cascade = CascadeType.ALL)
    private List<Daily> dailyList;

    @JsonIgnore
    @OneToMany(mappedBy = "person", targetEntity = Schedule.class, cascade = CascadeType.ALL)
    private Set<Schedule> schedules;
    @JsonIgnore
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();


    public Person(Long id) {
        this.id = id;
    }

    public String getFirtsName() {
        if (name != null) {
            return name.split(" ")[0];
        }
        return "";
    }

    public String getLastName() {
        if (name != null) {
            String[] separateNameBySpace = name.split(" ");

            return separateNameBySpace[separateNameBySpace.length - 1];
        }
        return "";
    }
}
