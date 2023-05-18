package br.com.ifce.easyflow.controller.dto.studyArea;

import br.com.ifce.easyflow.model.StudyArea;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class StudyAreaUpdateDTO {

    @ApiModelProperty(value = "StudyArea name", example = "Teste")
    @NotNull @NotEmpty
    private String study_area_name;

    public StudyArea toStudyArea(Long id){
        StudyArea studyArea = new StudyArea();
        studyArea.setId(id);
        studyArea.setName(this.study_area_name);

        return studyArea;
    }
}
