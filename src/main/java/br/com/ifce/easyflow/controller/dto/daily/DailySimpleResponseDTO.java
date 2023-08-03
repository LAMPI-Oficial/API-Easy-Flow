package br.com.ifce.easyflow.controller.dto.daily;

import br.com.ifce.easyflow.model.Daily;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailySimpleResponseDTO {

    private Long id;

    @JsonProperty("description")
    private String whatWasDoneTodayMessage;
    @JsonProperty("created_at")
    private LocalDate date;
    @JsonProperty("person_id")
    private Long personId;
    @JsonProperty("person_name")
    private String personName;

    public DailySimpleResponseDTO(Daily daily) {
        this.id = daily.getId();
        this.whatWasDoneTodayMessage = daily.getWhatWasDoneTodayMessage();
        this.date = daily.getDate();
        this.personId = daily.getPerson().getId();
        this.personName = daily.getPerson().getName();
    }

}
