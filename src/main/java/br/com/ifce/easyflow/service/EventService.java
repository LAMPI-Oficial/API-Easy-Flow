package br.com.ifce.easyflow.service;

import br.com.ifce.easyflow.controller.dto.event.EventRequestDTO;
import br.com.ifce.easyflow.controller.dto.event.EventResponseDTO;
import br.com.ifce.easyflow.model.Event;
import br.com.ifce.easyflow.repository.EventRepository;
import br.com.ifce.easyflow.service.exceptions.BadRequestException;
import br.com.ifce.easyflow.service.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    public List<EventResponseDTO> listAll(Pageable pageable) {
        return eventRepository.findAll(pageable).stream().map(EventResponseDTO::new).toList();
    }

    public EventResponseDTO listById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with given id"));
        return new EventResponseDTO(event);
    }
    public List<EventResponseDTO> listByDate(String date, Pageable pageable){

        try {
            LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
            return eventRepository.findByDate(localDate, pageable).stream().map(EventResponseDTO::new).toList();

        } catch (DateTimeParseException e) {

            throw new BadRequestException("The date format does not conform to the format: yyyy-MM-dd. " +
                    e.getMessage());
        }
    }
    public List<EventResponseDTO> listByDateTime(String date, String time, Pageable pageable){

        try {
            LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
            LocalTime localTime = LocalTime.parse(time, DateTimeFormatter.ISO_TIME);
            return eventRepository.findByDateTime(localDate, localTime, pageable).stream().map(EventResponseDTO::new).toList();
        } catch (DateTimeParseException e) {
            throw new BadRequestException("The date format does not conform to the format: yyyy-MM-dd or HH:mm " +
                    e.getMessage());
        }
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
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with given id"));
        Event eventUpdated = updateEventWhitEventUpdateDTO(event, eventRequestsDTO);

        return new EventResponseDTO(eventRepository.save(eventUpdated));
    }

    @Transactional
    public void delete(Long id){
        eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with given id"));
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
