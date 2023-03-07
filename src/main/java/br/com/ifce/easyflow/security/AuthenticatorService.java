package br.com.ifce.easyflow.security;

import br.com.ifce.easyflow.model.User;
import br.com.ifce.easyflow.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthenticatorService implements UserDetailsService {
    private final UserService userService;

    public AuthenticatorService(UserService userService){
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<User> user = userService.findByLogin(login);

        if(user.isPresent())
            return user.get();

        throw new UsernameNotFoundException("User Not Found");
    }
}