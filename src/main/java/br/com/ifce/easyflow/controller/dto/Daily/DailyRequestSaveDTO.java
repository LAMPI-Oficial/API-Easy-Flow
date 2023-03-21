package br.com.ifce.easyflow.controller.dto.Daily;

import br.com.ifce.easyflow.model.Person;
import br.com.ifce.easyflow.model.enums.DailyTaskStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class DailyRequestSaveDTO {
    @NotBlank
    private DailyTaskStatusEnum dailyTaskStatusEnum;

    @NotBlank @Size(min = 5)
    private String WhatWasDoneTodayMessage;
    private String AnyQuestionsMessage;
    private String FeedbackMessage;

    @NotBlank
    private LocalDateTime localDateTime;

    @NotBlank
    private Long personId;
}
