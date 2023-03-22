package br.com.ifce.easyflow.controller.dto.Daily;

import br.com.ifce.easyflow.model.Daily;
import br.com.ifce.easyflow.model.Person;
import br.com.ifce.easyflow.model.enums.DailyTaskStatusEnum;
import lombok.*;


import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class DailyResponseDTO {

    private Long id;
    private DailyTaskStatusEnum dailyTaskStatusEnum;
    private String whatWasDoneTodayMessage;
    private String anyQuestionsMessage;
    private String feedbackMessage;
    private LocalDate date;
    private Person person;

    public static DailyResponseDTO toResponseDTO(Daily daily){
        return new DailyResponseDTO(daily.getId(), daily.getDailyTaskStatusEnum(),
                daily.getWhatWasDoneTodayMessage(),
                daily.getAnyQuestionsMessage(),
                daily.getFeedbackMessage(),
                daily.getDate(),
                daily.getPerson());
    }

}
