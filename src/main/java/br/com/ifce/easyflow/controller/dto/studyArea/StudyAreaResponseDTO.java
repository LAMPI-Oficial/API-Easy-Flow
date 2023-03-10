package br.com.ifce.easyflow.controller.dto.studyArea;

import br.com.ifce.easyflow.controller.dto.person.PersonDTO;
import br.com.ifce.easyflow.model.StudyArea;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class StudyAreaResponseDTO {

    private Long id;
    private String study_area_name;

    @JsonProperty("tb_person")
    private PersonDTO personDTO;

    public StudyAreaResponseDTO(){

    }

    public StudyAreaResponseDTO(StudyArea StudyArea){
        this.id = StudyArea.getId();
        this.study_area_name= StudyArea.getName();

        if(StudyArea.getPerson() != null){
            this.personDTO = new PersonDTO(StudyArea.getPerson());
        }
    }

}
