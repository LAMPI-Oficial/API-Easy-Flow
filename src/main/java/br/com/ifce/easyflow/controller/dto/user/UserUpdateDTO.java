package br.com.ifce.easyflow.controller.dto.user;

import br.com.ifce.easyflow.model.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class UserUpdateDTO {

    @ApiModelProperty(value = "User Email", example = "user1@teste.com.br")
    @NotNull @NotEmpty
    private String login;

    public User toUser(Long id){
        User user = new User();
        user.setId(id);
        user.setLogin(this.login);

        return user;
    }
}
