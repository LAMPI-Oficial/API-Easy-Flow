package br.com.ifce.easyflow.controller.dto.user;

import br.com.ifce.easyflow.controller.dto.person.PersonDTO;
import br.com.ifce.easyflow.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserResponseDTO {

    private Long id;
    private String login;
    private Boolean active;

    @JsonProperty("person")
    private PersonDTO personDTO;

    public UserResponseDTO(){

    }

    public UserResponseDTO(User user){
        this.id = user.getId();
        this.login = user.getLogin();
        this.active = user.getActive();

        if(user.getPerson() != null){
            this.personDTO = new PersonDTO(user.getPerson());
        }
    }

}
