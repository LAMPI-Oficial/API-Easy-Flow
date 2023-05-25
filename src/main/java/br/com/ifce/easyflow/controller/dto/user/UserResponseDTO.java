package br.com.ifce.easyflow.controller.dto.user;

import br.com.ifce.easyflow.controller.dto.person.PersonDTO;
import br.com.ifce.easyflow.model.Course;
import br.com.ifce.easyflow.model.Person;
import br.com.ifce.easyflow.model.StudyArea;
import br.com.ifce.easyflow.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserResponseDTO {

    private Long id;
    private String login;

    @JsonProperty("tb_person")
    private Person personDTO;

    private Long studyArea_id;
    private String studyArea_name;

    private Long course_id;
    private String course_name;

    public UserResponseDTO(){

    }


    public UserResponseDTO(User user){
        this.id = user.getId();
        this.login = user.getLogin();
        this.personDTO = user.getPerson();
        this.studyArea_id = user.getPerson().getStudy_area().getId();
        this.studyArea_name = user.getPerson().getStudy_area().getName();
        this.course_id = user.getPerson().getCourse().getId();
        this.course_name = user.getPerson().getCourse().getName();
    }

    public void setPersonDTO(Person savedPerson) {
    }

}
