package br.com.ifce.easyflow.service.course;

import br.com.ifce.easyflow.controller.dto.course.CourseRequestDTO;
import br.com.ifce.easyflow.controller.dto.course.CourseUpdateDTO;
import br.com.ifce.easyflow.model.Course;
import br.com.ifce.easyflow.repository.CourseRepository;
import br.com.ifce.easyflow.service.CourseService;
import br.com.ifce.easyflow.service.exceptions.ConflictException;
import br.com.ifce.easyflow.service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class CourseServiceTest {

    @InjectMocks
    private CourseService courseService;

    @Mock
    private CourseRepository courseRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_ValidCourse_ReturnsSavedCourse() {
        Course course = createCourse();
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        Course savedCourse = courseService.save(course);

        Assertions.assertEquals(course, savedCourse);
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void save_DuplicateCourse_ThrowsConflictException() {
        Course course = createCourse();
        when(courseRepository.findByName(any(String.class))).thenReturn(Optional.of(course));

        Assertions.assertThrows(ConflictException.class, () -> courseService.save(course));
        verify(courseRepository, never()).save(course);
    }

    @Test
    void search_ReturnsListOfCourses() {
        List<Course> courses = createCourseList();
        when(courseRepository.findAll()).thenReturn(courses);

        List<Course> result = courseService.search();

        Assertions.assertEquals(courses, result);
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void searchByID_ExistingCourse_ReturnsCourse() {
        Course course = createCourse();
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));

        Course result = courseService.searchByID(1L);

        Assertions.assertEquals(course, result);
        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    void searchByID_NonExistingCourse_ThrowsResourceNotFoundException() {
        when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> courseService.searchByID(1L));
        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    void findByCourse_ExistingCourse_ReturnsCourse() {
        Course course = createCourse();
        when(courseRepository.findByName(any(String.class))).thenReturn(Optional.of(course));

        Course result = courseService.findByCourse("Course-Teste");

        Assertions.assertEquals(course, result);
        verify(courseRepository, times(1)).findByName("Course-Teste");
    }

    @Test
    void findByCourse_NonExistingCourse_ThrowsResourceNotFoundException() {
        when(courseRepository.findByName(any(String.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> courseService.findByCourse("Course-Teste"));
        verify(courseRepository, times(1)).findByName("Course-Teste");
    }

    @Test
    void update_ExistingCourse_ReturnsUpdatedCourse() {
        Course oldCourse = createCourse();
        CourseUpdateDTO updateDTO = createCourseUpdateDTO("New Course");

        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(oldCourse));
        when(courseRepository.findByName(any(String.class))).thenReturn(Optional.empty());
        when(courseRepository.save(any(Course.class))).thenReturn(oldCourse);

        Course updatedCourse = courseService.update(1L, updateDTO);

        Assertions.assertEquals(updateDTO.getCourse_name(), updatedCourse.getName());
        verify(courseRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).findByName("New Course");
        verify(courseRepository, times(1)).save(oldCourse);
    }

    @Test
    void update_NonExistingCourse_ThrowsResourceNotFoundException() {
        CourseUpdateDTO updateDTO = createCourseUpdateDTO("New Course");

        when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> courseService.update(1L, updateDTO));
        verify(courseRepository, times(1)).findById(1L);
        verify(courseRepository, never()).findByName("New Course");
        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    void update_DuplicateCourse_ThrowsConflictException() {
        Course oldCourse = createCourse();
        CourseUpdateDTO updateDTO = createCourseUpdateDTO("New Course");

        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(oldCourse));
        when(courseRepository.findByName(any(String.class))).thenReturn(Optional.of(oldCourse));

        Assertions.assertThrows(ConflictException.class, () -> courseService.update(1L, updateDTO));
        verify(courseRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).findByName("New Course");
        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    void delete_ExistingCourse_ReturnsTrue() {
        Course course = createCourse();
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));

        boolean result = courseService.delete(1L);

        Assertions.assertTrue(result);
        verify(courseRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).delete(course);
    }

    private Course createCourse() {
        Course course = new Course();
        course.setId(1L);
        course.setName("Course-Teste");
        return course;
    }

    private List<Course> createCourseList() {
        List<Course> courses = new ArrayList<>();
        courses.add(createCourse());
        return courses;
    }

    private CourseUpdateDTO createCourseUpdateDTO(String newName) {
        CourseUpdateDTO updateDTO = new CourseUpdateDTO();
        updateDTO.setCourse_name(newName);
        return updateDTO;
    }
}
