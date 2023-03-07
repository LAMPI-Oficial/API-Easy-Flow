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

import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    private UserController(UserService userService){
        this.userService = userService;
    }

    @ApiOperation(value = "Returns a list of users", tags = {"User"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @GetMapping
    public List<UserResponseDTO> search(){
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
    public ResponseEntity<Object> searchById(@PathVariable Long id) {
        Optional<User> user = this.userService.searchByID(id);

        return user.isPresent()
                ? ResponseEntity.ok(new UserResponseDTO(user.get()))
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                "User Not Found");
    }

    @ApiOperation(value = "Returns a user by login", tags = {"User"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 404, message = "User not found in database"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @RequestMapping(value = "/search_by_login", method = RequestMethod.GET)
    public ResponseEntity<Object> searchByLogin(@RequestParam String login) {
        Optional<User> user = this.userService.searchByLogin(login);

        return user.isPresent()
                ? ResponseEntity.ok(new UserResponseDTO(user.get()))
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                "User Not Found");
    }

    @ApiOperation(value = "Save a user", tags = {"User"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Saved reservation"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 409, message = "User login is already being used"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid UserRequestDTO userRequest, UriComponentsBuilder uriBuilder){
        if(userService.existsByLogin(userRequest.getLogin())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("UserName is already in use.");
        }

        User user = userRequest.toUser();
        this.userService.save(user);

        URI uri = uriBuilder.path("/users/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).body(new UserResponseDTO(user));
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
        Optional<User> user = this.userService.searchByID(id);

        if(user.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    "User Not Found");
        }

        if(!Objects.equals(user.get().getLogin(), userUpdateDTO.getLogin()) && userService.existsByLogin(userUpdateDTO.getLogin())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("UserName is already in use.");
        }

        user = this.userService.update(userUpdateDTO.toUser(id));

        return user.isPresent()
                ? ResponseEntity.ok(new UserResponseDTO(user.get()))
                :ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                "User Not Found");
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

}
