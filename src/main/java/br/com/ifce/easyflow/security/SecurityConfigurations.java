package br.com.ifce.easyflow.security;

import br.com.ifce.easyflow.service.UserService;

import java.util.Arrays;

import javax.annotation.security.PermitAll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@Configuration
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthenticatorService authenticatorService;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private UserService userService;
	
	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(authenticatorService)
				.passwordEncoder(new BCryptPasswordEncoder());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and()
			.authorizeRequests()
				.antMatchers(HttpMethod.POST, "/auth").permitAll()
				.antMatchers(HttpMethod.GET,"/study_area").permitAll()
				.antMatchers(HttpMethod.GET, "/courses").permitAll()
				.antMatchers(HttpMethod.POST, "/address").permitAll()
				.antMatchers(HttpMethod.POST, "/user/recoveryPassword").permitAll()
				.antMatchers(HttpMethod.POST, "/persons").permitAll()
				.antMatchers(HttpMethod.GET, "/actuator/**").permitAll()
				.antMatchers(HttpMethod.GET, "/api/**").permitAll()
                .antMatchers(HttpMethod.GET,
                        "/swagger-resources/**",
                        "/swagger-ui.html",
                        "/v2/api-docs",
                        "/webjars/**").permitAll()
                .anyRequest().authenticated()
				.and()
				.csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.addFilterBefore(new AuthenticatorFilter(tokenService, userService), UsernamePasswordAuthenticationFilter.class);
	}
	
	@Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*")); // Define as origens permitidas (pode ser ajustado para suas necessidades)
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE")); // Define os métodos HTTP permitidos
        configuration.setAllowedHeaders(Arrays.asList("*")); // Define os headers permitidos

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

	
	
	@Override
	public void configure(WebSecurity web) throws Exception {
	}
	
}






