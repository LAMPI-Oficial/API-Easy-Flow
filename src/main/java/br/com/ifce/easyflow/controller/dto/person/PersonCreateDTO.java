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

    @ApiModelProperty(value = "Persons name", example = "Maria Castro")
    private String name;

    @ApiModelProperty(value = "Person's email", example = "user22@teste.com.br")
    private String email;

    @ApiModelProperty(value = "Course index", example = "1")
    private Long course_id;

    @ApiModelProperty(value = "Person phone number", example = "(85) 98840-6679")
    private String phone;

    @ApiModelProperty(value = "Study Area index", example = "1")
    private Long study_area_id;

    @ApiModelProperty(value = "Person's password", example = "123456")
    private String password;

    @ApiModelProperty(value = "Person's repeated password", example = "123456")
    private String repeated_password;



    public PersonCreateDTO(){

    }

    public PersonCreateDTO(Person person){
        this.name = person.getName();
        this.password = person.getUser().getPassword();
        this.phone = person.getPhone();
        this.course_id = person.getCourse().getId();
        this.study_area_id = person.getStudy_area().getId();
        this.email = person.getEmail();
    }



}
