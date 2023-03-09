package br.com.ifce.easyflow.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "study_area")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class StudyArea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_area_id")
    private Long id;

    @Column(name = "study_area_name")
    private String study_area_name;

    @OneToOne(mappedBy = "study_area")
    @JoinColumn
    private Person person;

    public StudyArea(String study_area_name){
        this.study_area_name = study_area_name;
    }
}
