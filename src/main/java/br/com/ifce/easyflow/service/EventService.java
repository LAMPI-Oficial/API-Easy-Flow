package br.com.ifce.easyflow.service;

import br.com.ifce.easyflow.controller.dto.event.EventRequestDTO;
import br.com.ifce.easyflow.controller.dto.event.EventResponseDTO;
import br.com.ifce.easyflow.model.Event;
import br.com.ifce.easyflow.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    public Page<EventResponseDTO> listAll(Pageable pageable) {
        return eventRepository.findAll(pageable).map(EventResponseDTO::new);
    }

    public EventResponseDTO listById(Long id) {
        Event event = eventRepository.findById(id).orElseThrow();
        return new EventResponseDTO(event);
    }
    public Page<EventResponseDTO> listByDate(String date, Pageable pageable){
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return eventRepository.findByDate(localDate,pageable).map(EventResponseDTO::new);
    }
    public Page<EventResponseDTO> listByDateTime(String date, String time, Pageable pageable){
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalTime localTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH-mm-ss"));
        return eventRepository.findByDateTime(localDate, localTime,pageable).map(EventResponseDTO::new);
    }

    @Transactional
    public EventResponseDTO save(EventRequestDTO eventToSaveDTO){
        Event event = Event.builder()
                .description(eventToSaveDTO.getDescription())
                .imageUrl(eventToSaveDTO.getImageUrl())
                .date(eventToSaveDTO.getDate())
                .time(eventToSaveDTO.getTime())
                .build();

        return new EventResponseDTO(eventRepository.save(event));
    }
    @Transactional
    public EventResponseDTO update(Long id, EventRequestDTO eventRequestsDTO){
        Event event = eventRepository.findById(id).orElseThrow();
        Event eventUpdated = updateEventWhitEventUpdateDTO(event, eventRequestsDTO);

        return new EventResponseDTO(eventRepository.save(eventUpdated));
    }

    @Transactional
    public void delete(Long id){
        eventRepository.findById(id).orElseThrow();
        eventRepository.deleteById(id);
    }



    private static Event updateEventWhitEventUpdateDTO(Event event, EventRequestDTO eventRequestDTO){
        event.setDescription(eventRequestDTO.getDescription());
        event.setImageUrl(eventRequestDTO.getImageUrl());
        event.setDate(eventRequestDTO.getDate());
        event.setTime(eventRequestDTO.getTime());
        return event;
    }
}
