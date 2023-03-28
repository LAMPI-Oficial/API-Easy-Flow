package br.com.ifce.easyflow.controller.dto.solicitation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SolicitationPostRequestDTO {
    @Size(min = 10, max = 300, message = "The justification must be between 10 and 300 characters.")
    @NotBlank(message = "Justification cannot be empty or null")
    private String justification;

    @NotBlank(message = "The start date cannot be empty or null")
    @Future(message = "The start date cannot be earlier than today")
    @JsonProperty("start-date")
    private LocalDate startDate;

    @NotBlank(message = "The end date cannot be empty or null")
    @Future(message = "The end date cannot be earlier than today")
    @JsonProperty("end-date")
    private LocalDate endDate;

    @NotBlank(message = "The person id cannot be empty or null")
    @JsonProperty("person-id")
    private Long personId;

}
