package br.com.ifce.easyflow.controller.dto.Daily;

import br.com.ifce.easyflow.model.Daily;
import br.com.ifce.easyflow.model.Person;
import br.com.ifce.easyflow.model.enums.DailyTaskStatusEnum;
import lombok.*;


import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class DailyResponseDTO {

    private Long id;
    private DailyTaskStatusEnum dailyTaskStatusEnum;
    private String WhatWasDoneTodayMessage;
    private String AnyQuestionsMessage;
    private String FeedbackMessage;
    private LocalDateTime localDateTime;
    private Person person;

    public static DailyResponseDTO toResponseDTO(Daily daily){
        return new DailyResponseDTO(daily.getId(), daily.getDailyTaskStatusEnum(),
                daily.getWhatWasDoneTodayMessage(),
                daily.getAnyQuestionsMessage(),
                daily.getFeedbackMessage(),
                daily.getLocalDateTime(),
                daily.getPerson());
    }

}
