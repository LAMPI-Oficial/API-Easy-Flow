package br.com.ifce.easyflow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "course")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter 
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long id;

    @Column(name = "course_name")
    private String name;

    @OneToMany(mappedBy = "course",  targetEntity = Person.class, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Person> person;

    public Course(String course_name){
        this.name = course_name;
    }
}
