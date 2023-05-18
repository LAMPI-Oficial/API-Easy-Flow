package br.com.ifce.easyflow.controller.dto.daily;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyRequestSaveFeedbackDTO {
    private String feedbackMessage;
}
