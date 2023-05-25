package br.com.ifce.easyflow.controller.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventRequestDTO {
    @ApiModelProperty(value = "Description of the event")
    @NotBlank
    private String description;

    @ApiModelProperty(value = "Event image url")
    @NotBlank
    private String imageUrl;

    @ApiModelProperty(value = "Date of the event", notes = "The event date must follow the following pattern: yyyy-MM-dd")
    @NotBlank
    @FutureOrPresent
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate date;

    @ApiModelProperty(value = "Event time", notes = "The event time must follow the following pattern: HH:mm")
    @FutureOrPresent
    @JsonFormat(pattern="HH:mm")
    private LocalTime time;
}
