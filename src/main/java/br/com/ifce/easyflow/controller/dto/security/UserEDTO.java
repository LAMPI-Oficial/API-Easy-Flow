package br.com.ifce.easyflow.controller.dto.security;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class UserEDTO {

    @ApiModelProperty(value = "User Email", example = "user1@teste.com.br")
    @NotNull
    @NotEmpty
    private String login;

    @ApiModelProperty(value = "User Password", example = "123456")
    @NotNull
    @NotEmpty
    private String password;



    public UsernamePasswordAuthenticationToken convert() {
        return new UsernamePasswordAuthenticationToken(login, password);
    }
}