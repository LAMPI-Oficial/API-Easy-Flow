package br.com.ifce.easyflow.controller.dto.security;


import br.com.ifce.easyflow.controller.dto.user.UserResponseDTO;
import br.com.ifce.easyflow.model.Person;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PersonSecurityDTO {

    private TokenDTO token;
    private UserResponseDTO user;
    private Person person;
}
