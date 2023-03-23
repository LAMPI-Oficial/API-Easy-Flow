package br.com.ifce.easyflow.security;

import br.com.ifce.easyflow.model.User;
import br.com.ifce.easyflow.service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticatorFilter extends OncePerRequestFilter {
	
	private final TokenService tokenService;

	private final UserService userService;

	public AuthenticatorFilter(TokenService tokenService, UserService userService) {
		this.tokenService = tokenService;
		this.userService = userService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String token = getToken(request);
		boolean valid = tokenService.validToken(token);
		if (valid) {
			authenticateClient(token);
		}
		filterChain.doFilter(request, response);
	}

	private void authenticateClient(String token) {
		Long userId = tokenService.getUserId(token);
		User user = userService.searchByID(userId).get();
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	private String getToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");

		if (token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
			return null;
		}
		
		return token.substring(7, token.length());
	}

}