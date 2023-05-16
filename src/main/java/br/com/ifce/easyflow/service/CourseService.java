package br.com.ifce.easyflow.service;

import br.com.ifce.easyflow.controller.dto.course.CourseRequestDTO;
import br.com.ifce.easyflow.controller.dto.course.CourseUpdateDTO;
import br.com.ifce.easyflow.repository.CourseRepository;
import br.com.ifce.easyflow.model.Course;
import br.com.ifce.easyflow.service.exceptions.ConflictException;
import br.com.ifce.easyflow.service.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository CourseRepository) {
        this.courseRepository = CourseRepository;
    }

    @Transactional
    public Course save(Course course) {

        if (existsByCourse(course.getName())) {
            throw new ConflictException("A course with the given name already exists.");
        }
        return this.courseRepository.save(course);
    }

    public List<Course> search() {
        return this.courseRepository.findAll();
    }

    public Course searchByID(Long id) {
        return this.courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No courses were found with the given id."));
    }

    public Course findByCourse(String course_name) {
        return this.courseRepository.findByName(course_name)
                .orElseThrow(() -> new ResourceNotFoundException("No courses were found with the given name."));
    }

    @Transactional
    public Course update(Long id, CourseUpdateDTO courseUpdateDTO) {
        Course oldCourse = this.searchByID(id);

        if(!Objects.equals(oldCourse.getName(), courseUpdateDTO.getCourse_name())
                && this.existsByCourse(courseUpdateDTO.getCourse_name())){

            throw new ConflictException("A course with the given name already exists.");

        }

        oldCourse.setName(courseUpdateDTO.getCourse_name());

        return courseRepository.save(oldCourse);
    }

    @Transactional
    public Boolean delete(Long id) {
        Course course = this.searchByID(id);

        if (course != null) {
            this.courseRepository.delete(course);
            return true;
        }

        return false;
    }

    public Course searchByName(String course_name) {
        return this.courseRepository.findByName(course_name)
                .orElseThrow(() -> new ResourceNotFoundException("No courses were found with the given name."));
    }

    private Course fillUpdateCourse(Course oldCourse, Course newCourse) {
        newCourse.setName(oldCourse.getName());
        return newCourse;
    }

    public boolean existsByCourse(String course_name) {
        Optional<Course> exist = this.courseRepository.findByName(course_name);

        return exist.isPresent();
    }


    public boolean existsByID(Long id) {
        Optional<Course> exist = this.courseRepository.findById(id);

        return exist.isPresent();
    }
}
