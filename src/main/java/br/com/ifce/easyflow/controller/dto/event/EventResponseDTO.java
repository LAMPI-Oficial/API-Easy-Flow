package br.com.ifce.easyflow.controller.dto.event;

import br.com.ifce.easyflow.model.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EventResponseDTO {
    private Long id;

    private String description;

    private String imageUrl;

    private LocalDate date;
    private LocalTime time;

    public EventResponseDTO(Event event) {
        this.id = event.getId();
        this.description = event.getDescription();
        this.imageUrl= event.getImageUrl();
        this.date = event.getDate();
        this.time = event.getTime();
    }
}
