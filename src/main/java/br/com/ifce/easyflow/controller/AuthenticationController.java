package br.com.ifce.easyflow.controller;


import br.com.ifce.easyflow.config.ProblemDetails;
import br.com.ifce.easyflow.controller.dto.security.UserEDTO;
import br.com.ifce.easyflow.controller.dto.security.TokenDTO;
import br.com.ifce.easyflow.controller.dto.security.UserSecurityDTO;
import br.com.ifce.easyflow.controller.dto.user.UserResponseDTO;
import br.com.ifce.easyflow.model.User;
import br.com.ifce.easyflow.security.TokenService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.Instant;


@RestController
@RequestMapping("/auth")
public class AuthenticationController {

	@Autowired
	private AuthenticationManager authManager;

	@Autowired
	private TokenService tokenService;

	@ApiOperation(value = "Authenticate user", tags = {"Auth"})
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successful request"),
			@ApiResponse(code = 401, message = "Invalid Credentials"),
			@ApiResponse(code = 500, message = "Internal exception"),
	})
	@PostMapping
	public ResponseEntity<Object> autenticar(@RequestBody @Valid UserEDTO form) {
		UsernamePasswordAuthenticationToken login = form.convert();

		try {
			Authentication authentication = authManager.authenticate(login);

			TokenDTO token = new TokenDTO(tokenService.generateToken(authentication));

			UserResponseDTO user = new UserResponseDTO((User) authentication.getPrincipal());

			return ResponseEntity.ok(new UserSecurityDTO(token,user));
		} catch (AuthenticationException e) {
			e.printStackTrace();

			ProblemDetails problemDetails = ProblemDetails.builder()
					.title("Unauthorized exception")
					.detail("The email or password entered is invalid")
					.status(HttpStatus.UNAUTHORIZED.value())
					.timestamp(Instant.now())
					.build();
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problemDetails);
		}
	}

}