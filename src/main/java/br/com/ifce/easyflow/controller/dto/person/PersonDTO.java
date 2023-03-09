package br.com.ifce.easyflow.controller.dto.person;

import br.com.ifce.easyflow.controller.dto.validation.constraints.OnlyNumbers;
import br.com.ifce.easyflow.model.Person;
import br.com.ifce.easyflow.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter @Setter
public class PersonDTO {

    private Long id;

    @ApiModelProperty(value = "Person's name", example = "Maria Castro")
    private String name;

    @ApiModelProperty(value = "Person's email", example = "user23@teste.com.br")
    private String email;



    public PersonDTO(){

    }

    public PersonDTO(Person person){
        this.id = person.getId();
        this.name = person.getName();
        this.email = person.getEmail();
        
    }



}
