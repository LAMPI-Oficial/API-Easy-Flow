package br.com.ifce.easyflow.controller.dto.security;

import lombok.Data;

@Data
public class LoginDTO {
	
	private String user;
	private String pass;

}