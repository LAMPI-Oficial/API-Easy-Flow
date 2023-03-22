package br.com.ifce.easyflow.controller.dto.Daily;

import br.com.ifce.easyflow.model.Person;
import br.com.ifce.easyflow.model.enums.DailyTaskStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import springfox.documentation.spring.web.json.Json;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class DailyRequestSaveDTO {

    private DailyTaskStatusEnum dailyTaskStatusEnum;
    @NotBlank @Size(min = 5)
    private String whatWasDoneTodayMessage;
    private String anyQuestionsMessage;
    private String feedbackMessage;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT-3")
    private LocalDate date;

    private Long personId;
}
