package br.com.ifce.easyflow.controller;

import br.com.ifce.easyflow.controller.dto.course.CourseRequestDTO;
import br.com.ifce.easyflow.controller.dto.course.CourseResponseDTO;
import br.com.ifce.easyflow.controller.dto.course.CourseUpdateDTO;
import br.com.ifce.easyflow.model.Course;
import br.com.ifce.easyflow.service.CourseService;
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
    public ResponseEntity<Object> searchById(@PathVariable Long id) {
        Optional<Course> Course = this.courseService.searchByID(id);

        return Course.isPresent()
                ? ResponseEntity.ok(new CourseResponseDTO(Course.get()))
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                "Course Not Found");
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
        Optional<Course> Course = this.courseService.searchByName(name);

        return Course.isPresent()
                ? ResponseEntity.ok(new CourseResponseDTO(Course.get()))
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                "Course Not Found");
    }

    @ApiOperation(value = "Save a Course", tags = {"Course"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Saved reservation"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 409, message = "Course login is already being used"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid CourseRequestDTO CourseRequest, UriComponentsBuilder uriBuilder){
        if(courseService.existsByCourse(CourseRequest.getCourse_name())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Course Name is already in use.");
        }

        Course Course = CourseRequest.toCourse();
        this.courseService.save(Course);

        URI uri = uriBuilder.path("/courses/{id}").buildAndExpand(Course.getId()).toUri();
        return ResponseEntity.created(uri).body(new CourseResponseDTO(Course));
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
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody @Valid CourseUpdateDTO CourseUpdateDTO) {
        Optional<Course> Course = this.courseService.searchByID(id);

        if(Course.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    "Course Not Found");
        }

        if(!Objects.equals(Course.get().getName(), CourseUpdateDTO.getCourse_name()) && courseService.existsByCourse(CourseUpdateDTO.getCourse_name())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("CourseName is already in use.");
        }

        Course = this.courseService.update(CourseUpdateDTO.toCourse(id));

        return Course.isPresent()
                ? ResponseEntity.ok(new CourseResponseDTO(Course.get()))
                :ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                "Course Not Found");
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
