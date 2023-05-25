package br.com.ifce.easyflow.controller.dto.daily;

import br.com.ifce.easyflow.model.Daily;
import br.com.ifce.easyflow.model.Person;
import br.com.ifce.easyflow.model.enums.DailyTaskStatusEnum;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyResponseDTO {

    private Long id;
    private DailyTaskStatusEnum dailyTaskStatusEnum;
    private String whatWasDoneTodayMessage;
    private String anyQuestionsMessage;
    private String feedbackMessage;
    private LocalDate date;
    private Person person;

    public DailyResponseDTO(Daily daily){
        this.id = daily.getId();
        this.dailyTaskStatusEnum = daily.getDailyTaskStatusEnum();
        this.whatWasDoneTodayMessage = daily.getWhatWasDoneTodayMessage();
        this.anyQuestionsMessage = daily.getAnyQuestionsMessage();
        this.feedbackMessage = daily.getFeedbackMessage();
        this.date = daily.getDate();
        this.person = daily.getPerson();
    }
}
