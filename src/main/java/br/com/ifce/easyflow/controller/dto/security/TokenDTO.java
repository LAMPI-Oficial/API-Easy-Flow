package br.com.ifce.easyflow.controller.dto.security;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TokenDTO {

    private String value;
    private String type;

    public TokenDTO(String token) {
        this.value = token;
        this.type = "Bearer";
    }

}