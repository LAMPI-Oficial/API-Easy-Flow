package br.com.ifce.easyflow.controller.dto.solicitation;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
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
public class SolicitationPutRequestDTO {

    @ApiModelProperty(value = "Justification of the solicitation",
            notes = "The justification must be between 10 and 300 characters.")
    @Size(min = 10, max = 300, message = "The justification must be between 10 and 300 characters.")
    @NotBlank(message = "Justification cannot be empty or null")
    private String justification;

    @ApiModelProperty(value = "Solicitation start date",
            notes = "Obey ISO-8601 standard: yyyy-MM-dd", example = "2023-08-22")
    @NotBlank(message = "The start date cannot be empty or null")
    @Future(message = "The start date cannot be earlier than today")
    @JsonProperty("start-date")
    private String startDate;

    @ApiModelProperty(value = "Solicitation end date",
            notes = "Obey ISO-8601 standard: yyyy-MM-dd", example = "2023-08-28")
    @NotBlank(message = "The end date cannot be empty or null")
    @Future(message = "The end date cannot be earlier than today")
    @JsonProperty("end-date")
    private String endDate;

}
