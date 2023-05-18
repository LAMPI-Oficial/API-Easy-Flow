package br.com.ifce.easyflow.controller.dto.studyArea;

import br.com.ifce.easyflow.model.StudyArea;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class StudyAreaRequestDTO {

    @ApiModelProperty(value = "StudyArea name", example = "Back-end")
    @NotNull @NotEmpty
    @JsonProperty(value = "study-area-name")
    private String StudyArea_name;

    public StudyArea toStudyArea(){
        return new StudyArea(StudyArea_name);
    }
    
}
