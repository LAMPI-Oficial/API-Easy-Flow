package br.com.ifce.easyflow.service;

import br.com.ifce.easyflow.controller.dto.user.UserRecoveryPassword;
import br.com.ifce.easyflow.controller.dto.user.UserRequestDTO;
import br.com.ifce.easyflow.controller.dto.user.UserUpdateDTO;
import br.com.ifce.easyflow.model.User;
import br.com.ifce.easyflow.repository.UserRepository;
import br.com.ifce.easyflow.service.exceptions.ConflictException;
import br.com.ifce.easyflow.service.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;

import javax.transaction.Transactional;
import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JavaMailSender emailSender;

    @Autowired
    public UserService(UserRepository userRepository, JavaMailSender emailSender) {
        this.userRepository = userRepository;
        this.emailSender = emailSender;
    }

    @Transactional
    public User save(UserRequestDTO userRequest) {

        if(existsByLogin(userRequest.getLogin())){
            throw new ConflictException("The  email provided is already being used.");
        }

        User newUser = userRequest.toUser();

        return this.userRepository.save(newUser);

    }

    public List<User> search() {
        return this.userRepository.findAll();
    }

    public User searchByID(Long id) {
        return this.userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("The user was not found in the database, " +
                        "please check the registered user."));
    }

    public User findByLogin(String login) {
        return this.userRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("No user with that email was found in the database, " +
                        "check the registered users."));
    }

    public boolean findByRecovery(UserRecoveryPassword userRecoveryPassword){
        String email = this.userRepository.findByLogin(userRecoveryPassword.getLogin()).get().getLogin();
        Long id = this.userRepository.findByLogin(userRecoveryPassword.getLogin()).get().getId();

        if(email.equals(userRecoveryPassword.getLogin()) && id.equals(userRecoveryPassword.getId())){
            
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            
            User user = this.newPassword(userRecoveryPassword.getId());

            mailMessage.setTo(user.getPerson().getEmail());
            mailMessage.setSubject("Email de recuperação de Senha");
            mailMessage.setText("Sua nova senha: " + user.getPassword() + "\n\nFaça login e altera sua senha atual\n\n");
            mailMessage.setFrom("marcos.junior@darmlabs.ifce.edu.br");
            emailSender.send(mailMessage);

            return true;
        }
        return false;
    }

    @Transactional
    public User update(Long id, UserUpdateDTO requestDTO) {
        User oldUser = this.searchByID(id);

        if (!oldUser.getLogin().equals(requestDTO.getLogin())
                && this.existsByLogin(requestDTO.getLogin())) {

            throw new ConflictException("The email provided is already being used.");
        }
        User newUser = requestDTO.toUser(id);
        newUser.setLogin(requestDTO.getLogin());
        return userRepository.save(this.fillUpdateUser(oldUser, newUser));
    }

    @Transactional
    public Boolean delete(Long id) {
        User user = this.searchByID(id);

        this.userRepository.delete(user);
        return true;

    }

    public User searchByLogin(String login) {
        return this.findByLogin(login);
    }

    private User fillUpdateUser(User oldUser, User newUser) {
        newUser.setPassword(oldUser.getPassword());
        return newUser;
    }

    public boolean existsByLogin(String login) {
        Optional<User> exist = this.userRepository.findByLogin(login);

        return exist.isPresent();
    }


    public boolean existsByID(Long id) {
        Optional<User> exist = this.userRepository.findById(id);

        return exist.isPresent();
    }

    public String generatePassword() {
        String CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+-=[]{}|;:,.<>?";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(CHARSET.length());
            sb.append(CHARSET.charAt(index));
        }
        return sb.toString();
    }

    public User newPassword(Long id) {

        User user = searchByID(id);
        String newPassword = this.generatePassword();
        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));

        return userRepository.save(user);
    }
}
