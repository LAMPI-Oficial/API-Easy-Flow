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

    @ApiModelProperty(value = "Person's CPF", example = "12345678995")
    private String cpf;

    @ApiModelProperty(value = "Person's email", example = "user23@teste.com.br")
    private String email;

    @ApiModelProperty(value = "Person's visitor status", example = "false")
    @JsonProperty("is_visitor")
    private boolean isVisitor;

    @ApiModelProperty(value = "Person's admin status", example = "false")
    @Column(name = "is_admin")
    private boolean isAdmin;
    public PersonDTO(){

    }

    public PersonDTO(Person person){
        this.id = person.getId();
        this.name = person.getName();
        this.cpf = person.getCpf();
        this.email = person.getEmail();
        this.isVisitor = person.isVisitor();
        this.isAdmin = person.isAdmin();
    }



}
