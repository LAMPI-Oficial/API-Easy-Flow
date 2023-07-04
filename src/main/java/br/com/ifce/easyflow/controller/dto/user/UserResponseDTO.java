package br.com.ifce.easyflow.controller.dto.user;

import br.com.ifce.easyflow.controller.dto.person.PersonDTO;
import br.com.ifce.easyflow.model.Address;
import br.com.ifce.easyflow.model.Course;
import br.com.ifce.easyflow.model.Person;
import br.com.ifce.easyflow.model.StudyArea;
import br.com.ifce.easyflow.model.User;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserResponseDTO {

    private Long id;
    private String login;

    @JsonProperty("person")
    private Person personDTO;

    @JsonProperty("studyArea")
        private StudyArea studyArea;

    @JsonProperty("course")
        private Course course;

    @JsonProperty("address")
    private List<Address> address;

    public UserResponseDTO(){

    }

    public UserResponseDTO(User user){
        this.id = user.getId();
        this.login = user.getLogin();
        this.personDTO = user.getPerson();
        this.studyArea = user.getPerson().getStudy_area();
        this.course = user.getPerson().getCourse();
        this.address = user.getPerson().getAddresses();
    }

    public void setPersonDTO(Person savedPerson) {
    }

}
