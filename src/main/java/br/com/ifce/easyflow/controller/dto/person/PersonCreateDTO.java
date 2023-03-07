package br.com.ifce.easyflow.controller.dto.person;

import br.com.ifce.easyflow.controller.dto.validation.constraints.OnlyNumbers;
import br.com.ifce.easyflow.model.Person;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter @Setter
public class PersonCreateDTO {

    @ApiModelProperty(hidden = true)
    private Long id;

    @ApiModelProperty(value = "Persons name", example = "Maria Castro")
    private String name;

    @ApiModelProperty(value = "Person's CPF", example = "12345678995")
    private String cpf;

    @ApiModelProperty(value = "Person's phone", example = "88985455632")
    private String phone;

    @ApiModelProperty(value = "Person's email", example = "user22@teste.com.br")
    private String email;

    @ApiModelProperty(value = "Person's password", example = "123456")
    private String password;

    @ApiModelProperty(value = "Person's repeated password", example = "123456")
    private String repeated_password;

    public PersonCreateDTO(){

    }

    public PersonCreateDTO(Person person){
        this.name = person.getName();
        this.cpf = person.getCpf();
        this.password = person.getUser().getPassword();
        this.email = person.getEmail();
        this.phone = person.getPhone();
    }



}
