package br.com.ifce.easyflow.controller.dto.daily;

import br.com.ifce.easyflow.model.Daily;
import br.com.ifce.easyflow.model.Person;
import br.com.ifce.easyflow.model.enums.DailyTaskStatusEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class DailyResponseDTO {

    private Long id;
    private DailyTaskStatusEnum dailyTaskStatusEnum;
    private String whatWasDoneTodayMessage;
    private String anyQuestionsMessage;
    private String feedbackMessage;
    private LocalDate date;
    private Person person;


    public DailyResponseDTO(Long id, DailyTaskStatusEnum dailyTaskStatusEnum, String whatWasDoneTodayMessage, String anyQuestionsMessage, String feedbackMessage, LocalDate date, Person person) {
        this.id = id;
        this.dailyTaskStatusEnum = dailyTaskStatusEnum;
        this.whatWasDoneTodayMessage = whatWasDoneTodayMessage;
        this.anyQuestionsMessage = anyQuestionsMessage;
        this.feedbackMessage = feedbackMessage;
        this.date = date;
        this.person = person;
    }

    public static DailyResponseDTO toResponseDTO(Daily daily) {
        return new DailyResponseDTO(daily.getId(), daily.getDailyTaskStatusEnum(),
                daily.getWhatWasDoneTodayMessage(),
                daily.getAnyQuestionsMessage(),
                daily.getFeedbackMessage(),
                daily.getDate(),
                daily.getPerson());
    }

}
