package br.com.ifce.easyflow.controller;


import br.com.ifce.easyflow.controller.dto.address.AddressRequestDTO;
import br.com.ifce.easyflow.controller.dto.person.PersonCreateDTO;
import br.com.ifce.easyflow.controller.dto.person.PersonDTO;
import br.com.ifce.easyflow.controller.dto.person.PersonSaveRequest;
import br.com.ifce.easyflow.controller.dto.security.PersonSecurityDTO;
import br.com.ifce.easyflow.controller.dto.security.TokenDTO;
import br.com.ifce.easyflow.controller.dto.user.UserResponseDTO;
import br.com.ifce.easyflow.model.Address;
import br.com.ifce.easyflow.model.Person;
import br.com.ifce.easyflow.model.User;
import br.com.ifce.easyflow.security.TokenService;
import br.com.ifce.easyflow.service.AddressService;
import br.com.ifce.easyflow.service.CourseService;
import br.com.ifce.easyflow.service.PersonService;
import br.com.ifce.easyflow.service.StudyAreaService;
import br.com.ifce.easyflow.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/persons")
public class PersonController {

    private final PersonService personService;
    private final UserService userService;
    private final CourseService courseService;
    private final StudyAreaService studyAreaService;
    private final AddressService addressService;


    @Autowired
    private PersonController(PersonService personService, UserService userService, CourseService courseService, StudyAreaService studyAreaService, AddressService addressService) {
        this.personService = personService;
        this.userService = userService;
        this.courseService = courseService;
        this.studyAreaService = studyAreaService;
        this.addressService = addressService;
    }

    @ApiOperation(value = "Save a person", tags = {"Person"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Person saved"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 400, message = "Person cannot be saved, passwords do not match"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid PersonSaveRequest personSaveRequest) {
        PersonCreateDTO personCreateDTO = personSaveRequest.getPersonCreateDTO();
        AddressRequestDTO addressRequestDTO = personSaveRequest.getAddressRequestDTO();

        PersonSecurityDTO personSecurityDTO = personService.createPerson(personCreateDTO,addressRequestDTO);

        return ResponseEntity.ok(personSecurityDTO);
    }
    @ApiOperation(value = "Save a person representant", tags = {"Person"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Person saved"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 400, message = "Person cannot be saved, passwords do not match"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @PostMapping("savedRepresentant/{id}")
    public ResponseEntity<Object> save_representant(@PathVariable Long id) {
        Person person = personService.findById(id);
        person.setPerson_representant(true);
        Person savedPerson = this.personService.save(person);
        return ResponseEntity.ok(savedPerson);
    }
    @ApiOperation(value = "Returns a list of representants", tags = {"Person"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @GetMapping("representants")
    public List<PersonDTO> findAllRepresentants() {
        return this.personService
                .findAllRepresentants()
                .stream()
                .map(PersonDTO::new)
                .collect(Collectors.toList());
    }


    @ApiOperation(value = "Returns a list of persons", tags = {"Person"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @GetMapping
    public List<PersonDTO> findAll() {
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
    public ResponseEntity<PersonDTO> searchById(@PathVariable Long id) {
        Person person = this.personService.findById(id);

        return ResponseEntity.ok(new PersonDTO(person));
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
    public ResponseEntity<Person> update(@PathVariable Long id, @RequestBody @Valid PersonDTO personDTO) {

        return ResponseEntity.ok(personService.update(id, personDTO));
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
