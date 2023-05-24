package br.com.ifce.easyflow.controller.dto.user;

import br.com.ifce.easyflow.controller.dto.person.PersonDTO;
import br.com.ifce.easyflow.model.Person;
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
    public UserResponseDTO(){

    }

    public UserResponseDTO(User user){
        this.id = user.getId();
        this.login = user.getLogin();
        this.personDTO = user.getPerson();
    }

    public void setPersonDTO(Person savedPerson) {
    }

}
