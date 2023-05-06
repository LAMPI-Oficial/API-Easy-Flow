package br.com.ifce.easyflow.controller.dto.course;

import br.com.ifce.easyflow.controller.dto.person.PersonDTO;
import br.com.ifce.easyflow.model.Course;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CourseResponseDTO {

    private Long id;
    private String course_name;

    // @JsonProperty("tb_person")
    // private PersonDTO personDTO;

    public CourseResponseDTO(){

    }

    public CourseResponseDTO(Course Course){
        this.id = Course.getId();
        this.course_name= Course.getName();

        // if(Course.getPerson() != null){
        //     this.personDTO = new PersonDTO(Course.getClass());
        // }
    }

}
