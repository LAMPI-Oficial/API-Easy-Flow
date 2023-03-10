package br.com.ifce.easyflow.controller;


import br.com.ifce.easyflow.controller.dto.user.UserRequestDTO;
import br.com.ifce.easyflow.controller.dto.person.PersonCreateDTO;
import br.com.ifce.easyflow.controller.dto.person.PersonDTO;
import br.com.ifce.easyflow.controller.dto.security.UserEDTO;
import br.com.ifce.easyflow.model.Person;
import br.com.ifce.easyflow.model.User;
import br.com.ifce.easyflow.service.PersonService;
import br.com.ifce.easyflow.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/persons")
public class PersonController {

    private final PersonService personService;
    private final UserService userService;

    @Autowired
    private PersonController(PersonService personService, UserService userService){
        this.personService = personService;
        this.userService = userService;
    }

    @ApiOperation(value = "Save a person", tags = {"Person"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Person saved"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 400, message = "Person cannot be saved, passwords do not match"),
            @ApiResponse(code = 409, message = "Person already exists in the database"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid PersonCreateDTO personCreateDTO){
        if(!personCreateDTO.getPassword().equals(personCreateDTO.getRepeated_password())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Passwords does not match.");
        }
        if(personService.existsByEmail(personCreateDTO.getEmail())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email is already in use.");
        }
        Person person = personService.createPerson(personCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.personService.save(person));
    }

    @ApiOperation(value = "Returns a list of persons", tags = {"Person"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @GetMapping
    public List<PersonDTO> findAll(){
        return this.personService
                .findAll()
                .stream()
                .map(PersonDTO::new)
                .collect(Collectors.toList());
    }
    @ApiOperation(value = "Returns a person by id", tags = {"Person"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 404, message = "Person not found in database"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> searchById(@PathVariable Long id) {
        Optional<Person> person = this.personService.findById(id);

        return person.isPresent()
                ? ResponseEntity.ok(new PersonDTO(person.get()))
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                "Person Not Found");
    }

    @ApiOperation(value = "Update a person", tags = {"Person"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 404, message = "Person not found in database"),
            @ApiResponse(code = 409, message = "Person already exists in the database"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody @Valid PersonDTO personDTO) {
        Optional<Person> person = this.personService.findById(id);

        if(person.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    "Person Not Found");
        }

        if(!Objects.equals(person.get().getEmail(), personDTO.getEmail()) && personService.existsByEmail(personDTO.getEmail())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Person CPF is already in use.");
        }
        person.get().setId(id);
        person.get().setName(personDTO.getName());
        person.get().setEmail(personDTO.getEmail());

        return ResponseEntity.ok(personService.update(person.get()));
    }

    @ApiOperation(value = "Delete a person", tags = {"Person"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 404, message = "Person not found in database"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        boolean removed = this.personService.delete(id);
        return removed
                ? ResponseEntity.status(HttpStatus.OK).body(
                        "Person was deleted")
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                "Person Not Found");
    }

}
