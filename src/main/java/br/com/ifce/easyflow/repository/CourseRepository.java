package br.com.ifce.easyflow.repository;

import br.com.ifce.easyflow.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course,Long> {

    Optional<Course> findByName(String course_name);
    @Override
    Optional<Course> findById(Long id);
}
