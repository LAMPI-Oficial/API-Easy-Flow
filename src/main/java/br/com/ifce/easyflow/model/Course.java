package br.com.ifce.easyflow.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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

    @OneToOne(mappedBy = "course")
    @JoinColumn
    private Person person;

    public Course(String course_name){
        this.name = course_name;
    }
}
