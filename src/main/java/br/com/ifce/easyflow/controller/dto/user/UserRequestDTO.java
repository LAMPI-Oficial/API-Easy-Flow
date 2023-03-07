package br.com.ifce.easyflow.controller.dto.user;

import br.com.ifce.easyflow.model.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class UserRequestDTO {

    @ApiModelProperty(value = "User Email", example = "user1@teste.com.br")
    @NotNull @NotEmpty
    private String login;

    @ApiModelProperty(value = "User Password", example = "123456")
    @NotNull @NotEmpty
    private String password;

    public User toUser(){
        password = new BCryptPasswordEncoder().encode(password);
        return new User(login,password,true);
    }

//    public User toUser(Long id){
//        return new User(id,login,password,true,null);
//    }
}
