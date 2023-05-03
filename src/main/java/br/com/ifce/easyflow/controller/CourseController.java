package br.com.ifce.easyflow.controller;

import br.com.ifce.easyflow.controller.dto.course.CourseRequestDTO;
import br.com.ifce.easyflow.controller.dto.course.CourseResponseDTO;
import br.com.ifce.easyflow.controller.dto.course.CourseUpdateDTO;
import br.com.ifce.easyflow.model.Course;
import br.com.ifce.easyflow.service.CourseService;

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
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    private CourseController(CourseService courseService){
        this.courseService = courseService;
    }

    @ApiOperation(value = "Returns a list of Courses", tags = {"Course"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @GetMapping
    public List<CourseResponseDTO> search(){
        return this.courseService
                .search()
                .stream()
                .map(CourseResponseDTO::new)
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "Returns a Course by id", tags = {"Course"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 404, message = "Course not found in database"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> searchById(@PathVariable Long id) {
        Course course = this.courseService.searchByID(id);

        return ResponseEntity.ok(new CourseResponseDTO(course));
    }

    @ApiOperation(value = "Returns a Course by login", tags = {"Course"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 404, message = "Course not found in database"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @RequestMapping(value = "/search_by_name", method = RequestMethod.GET)
    public ResponseEntity<Object> searchByName(@RequestParam String name) {
        Course course = this.courseService.searchByName(name);

        return ResponseEntity.ok(new CourseResponseDTO(course));
    }

    @ApiOperation(value = "Save a Course", tags = {"Course"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Saved reservation"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 409, message = "Course login is already being used"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @PostMapping
    public ResponseEntity<CourseResponseDTO> save(@RequestBody @Valid CourseRequestDTO courseRequest, UriComponentsBuilder uriBuilder){

       Course course = this.courseService.save(courseRequest);

        URI uri = uriBuilder.path("/courses/{id}").buildAndExpand(course).toUri();
        return ResponseEntity.created(uri).body(new CourseResponseDTO(course));
    }

    @ApiOperation(value = "Update a Course by id", tags = {"Course"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Saved reservation"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 404, message = "Course not found in database"),
            @ApiResponse(code = 409, message = "Course login is already being used"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> update(@PathVariable Long id, @RequestBody @Valid CourseUpdateDTO courseUpdateDTO) {

        Course course = this.courseService.update(id, courseUpdateDTO);

        return ResponseEntity.ok(new CourseResponseDTO(course));
    }

    @ApiOperation(value = "Delete a Course by id", tags = {"Course"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 404, message = "Course not found in database"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        boolean removed = this.courseService.delete(id);

        return removed
                ? ResponseEntity.status(HttpStatus.OK).body(
                        "Course was deleted")
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                "Course Not Found");
    }

}
