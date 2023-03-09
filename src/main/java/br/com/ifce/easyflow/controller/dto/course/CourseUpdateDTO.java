package br.com.ifce.easyflow.controller.dto.course;

import br.com.ifce.easyflow.model.Course;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class CourseUpdateDTO {

    @ApiModelProperty(value = "Course name", example = "Teste")
    @NotNull @NotEmpty
    private String course_name;

    public Course toCourse(Long id){
        Course Course = new Course();
        Course.setId(id);
        Course.setCourse_name(this.course_name);

        return Course;
    }
}
