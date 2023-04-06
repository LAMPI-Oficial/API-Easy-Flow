package br.com.ifce.easyflow.controller.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestDTO {
    private String description;

    private String imageUrl;

    private LocalDate date;
    private LocalTime time;
}
