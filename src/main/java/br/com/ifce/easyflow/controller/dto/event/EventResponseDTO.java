package br.com.ifce.easyflow.controller.dto.event;

import javax.persistence.Column;
import java.time.LocalDateTime;

public class EventResponseDTO {
    private Long id;

    private String description;

    private String imageUrl;

    private LocalDateTime dateTime;
}
