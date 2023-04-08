package br.com.ifce.easyflow.controller;

import br.com.ifce.easyflow.controller.dto.user.UserRequestDTO;
import br.com.ifce.easyflow.controller.dto.user.UserResponseDTO;
import br.com.ifce.easyflow.controller.dto.user.UserUpdateDTO;
import br.com.ifce.easyflow.model.User;
import br.com.ifce.easyflow.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final JavaMailSender emailSender;

    @Autowired
    private UserController(UserService userService, JavaMailSender emailSender) {
        this.userService = userService;
        this.emailSender = emailSender;
    }

    @ApiOperation(value = "Returns a list of users", tags = {"User"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @GetMapping
    public List<UserResponseDTO> search() {
        return this.userService
                .search()
                .stream()
                .map(UserResponseDTO::new)
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "Returns a user by id", tags = {"User"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 404, message = "User not found in database"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> searchById(@PathVariable Long id) {
        User user = this.userService.searchByID(id);

        return ResponseEntity.ok(new UserResponseDTO(user));
    }

    @ApiOperation(value = "Returns a user by login", tags = {"User"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 404, message = "User not found in database"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @RequestMapping(value = "/search_by_login", method = RequestMethod.GET)
    public ResponseEntity<UserResponseDTO> searchByLogin(@RequestParam String login) {
        User user = this.userService.searchByLogin(login);

        return ResponseEntity.ok(new UserResponseDTO(user));
    }

    @ApiOperation(value = "Save a user", tags = {"User"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Saved reservation"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 409, message = "User login is already being used"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @PostMapping
    public ResponseEntity<UserResponseDTO> save(@RequestBody @Valid UserRequestDTO userRequest, UriComponentsBuilder uriBuilder) {

        User userSaved = this.userService.save(userRequest);

        URI uri = uriBuilder.path("/users/{id}").buildAndExpand(userSaved.getId()).toUri();
        return ResponseEntity.created(uri).body(new UserResponseDTO(userSaved));
    }

    @ApiOperation(value = "Update a user by id", tags = {"User"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Saved reservation"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 404, message = "User not found in database"),
            @ApiResponse(code = 409, message = "User login is already being used"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody @Valid UserUpdateDTO userUpdateDTO) {

        User user = this.userService.update(id, userUpdateDTO);

        return ResponseEntity.ok(new UserResponseDTO(user));
    }

    @ApiOperation(value = "Delete a user by id", tags = {"User"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 404, message = "User not found in database"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        boolean removed = this.userService.delete(id);

        return removed
                ? ResponseEntity.status(HttpStatus.OK).body(
                "User was deleted")
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                "User Not Found");
    }

    @ApiOperation(value = "User recovery password", tags = {"User"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Saved reservation"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 409, message = "User login is already being used"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @GetMapping("/recoveryPassword/{id}")
    public Boolean sendEmail(@PathVariable Long id) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();

       User user = userService.newPassword(id);

        mailMessage.setTo(user.getPerson().getEmail());
        mailMessage.setSubject("Email de recuperação de Senha");
        mailMessage.setText("Sua nova senha: " + user.getPassword() + "\n\nFaça login e altera sua senha atual\n\n");
        mailMessage.setFrom("marcos.junior@darmlabs.ifce.edu.br");

        emailSender.send(mailMessage);

        return true;
    }
}
