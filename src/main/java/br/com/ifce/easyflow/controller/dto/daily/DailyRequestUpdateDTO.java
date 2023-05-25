package br.com.ifce.easyflow.controller.dto.daily;

import br.com.ifce.easyflow.model.enums.DailyTaskStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DailyRequestUpdateDTO {

    private DailyTaskStatusEnum dailyTaskStatusEnum;
    @NotBlank @Size(min = 5)
    private String whatWasDoneTodayMessage;
    private String anyQuestionsMessage;

}
