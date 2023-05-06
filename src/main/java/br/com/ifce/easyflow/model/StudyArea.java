package br.com.ifce.easyflow.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "study_area")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter 
@NoArgsConstructor 
@AllArgsConstructor
public class StudyArea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_area_id")
    private Long id;

    @Column(name = "study_area_name")
    private String name;

    @OneToMany(mappedBy = "study_area", targetEntity = Person.class, cascade = CascadeType.ALL)
    private List<Person> person;

    public StudyArea(String study_area_name){
        this.name = study_area_name;
    }
}
