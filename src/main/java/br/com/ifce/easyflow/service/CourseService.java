package br.com.ifce.easyflow.service;

import br.com.ifce.easyflow.repository.CourseRepository;
import br.com.ifce.easyflow.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository CourseRepository){
        this.courseRepository = CourseRepository;
    }

    @Transactional
    public Course save(Course Course){
        return this.courseRepository.save(Course);
    }

    public List<Course> search(){
        return this.courseRepository.findAll();
    }

    public Optional<Course> searchByID(Long id){
        return this.courseRepository.findById(id);
    }

    public Optional<Course> findByCourse(String course_name){
        return this.courseRepository.findByCourse_name(course_name);
    }

    @Transactional
    public Optional<Course> update(Course newCourse){
        Optional<Course> oldCourse = this.searchByID(newCourse.getId());

        return oldCourse.isPresent()
                ? Optional.of(this.save(this.fillUpdateCourse(oldCourse.get(),newCourse)))
                : Optional.empty();
    }

    @Transactional
    public Boolean delete(Long id){
        Optional<Course> Course = this.searchByID(id);

        if(Course.isPresent()){
            this.courseRepository.delete(Course.get());
            return true;
        }

        return false;
    }

    public Optional<Course> searchByName(String course_name){
        return this.courseRepository.findByCourseName(course_name);
    }

    private Course fillUpdateCourse(Course oldCourse,Course newCourse){
        newCourse.setCourse_name(oldCourse.getCourse_name());
        return newCourse;
    }

    public boolean existsByCourse(String course_name) {
        Optional<Course> exist = this.courseRepository.findByCourseName(course_name);

        return exist.isPresent();
    }


    public boolean existsByID(Long id) {
        Optional<Course> exist = this.courseRepository.findById(id);

        return exist.isPresent();
    }
}
